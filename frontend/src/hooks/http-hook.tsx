import { useState, useCallback, useContext } from "react";
import axios from "axios";
import { UserLoginContext } from "src/context/userLoginContext";
import { isAuthenticationNeeded } from "src/constants/endpoints";

export const useHttpClient = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const { jwtToken } = useContext(UserLoginContext);

  const sendRequest = useCallback(
    async (path = "", method = "GET", body: {}, headers = {}) => {
      setIsLoading(true);
      const config = isAuthenticationNeeded(path)
        ? headers
        : { Authorization: `Bearer ${jwtToken}` };
      isAuthenticationNeeded(path);

      const host = "http://localhost:8080";
      const url = host + path;
      let res = null;

      try {
        switch (method) {
          case "GET":
            res = await axios.get(url, config);
            break;
          case "POST":
            res = await axios.post(url, body);
            break;
          case "DELETE":
            res = await axios.delete(url, { headers: config, data: { body } });
            break;
          case "PATCH":
            res = await axios.patch(url, body, config);
            break;
          default:
            break;
        }
        setIsLoading(false);
        return res?.data;
      } catch (err: any) {
        setError(err.response.data.message);
        setIsLoading(false);
        throw err;
      }
    },
    []
  );

  const clearError = () => {
    setError(null);
  };

  return { isLoading, error, sendRequest, clearError };
};
