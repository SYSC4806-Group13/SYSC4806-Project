import { useState, useCallback, useContext } from "react";
import axios from "axios";
import { isAuthenticationNeeded } from "src/constants/endpoints";
import { UserLoginContext } from "src/context/userLoginContext";
import { httpMethod } from "src/constants/common";

export const useHttpClient = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const { logOut } = useContext(UserLoginContext);

  const host = "http://localhost:8080";

  const sendRequest = useCallback(
    async (path = "", method: httpMethod = "GET", body: {}) => {
      setIsLoading(true);

      let requestHeaders = isAuthenticationNeeded(method, path)
        ? { Authorization: "Bearer " + localStorage.getItem("amazin_jwt") }
        : {};

      const url = host + path;
      let res = null;
      let config = {
        headers: requestHeaders,
      };
      try {
        switch (method) {
          case "GET":
            res = await axios.get(url, config);
            break;
          case "POST":
            res = await axios.post(url, body, config);
            break;
          case "DELETE":
            res = await axios.delete(url, {
              headers: requestHeaders,
              data: { body },
            });
            break;
          case "PATCH":
            res = await axios.patch(url, body, config);
            break;
          case "PUT":
            res = await axios.put(url, body, config);
            break;
          default:
            break;
        }
        setIsLoading(false);
        return res?.data;
      } catch (err: any) {
        if (err.response.data.status === 401) {
          logOut();
        }
        setIsLoading(false);
        throw err;
      }
    },
    [logOut]
  );

  const clearError = () => {
    setError(null);
  };

  return { host, isLoading, error, sendRequest, clearError };
};
