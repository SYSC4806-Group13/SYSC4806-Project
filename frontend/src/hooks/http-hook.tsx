import { useState, useCallback } from "react";
import axios from "axios";

export const useHttpClient = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const sendRequest = useCallback(
        async (path = "", method = "GET", body : {}, headers = {}) => {
            setIsLoading(true);
            const host = "http://localhost:8080"
            const url = host + path;
            let res = null;
            try {
                switch (method) {
                    case "GET":
                        res = await axios.get(url);
                        break;
                    case "POST":
                        res = await axios.post(url, body);
                        break;
                    case "DELETE":
                        res = await axios.delete(url, { data: body });
                        break;
                    case "PATCH":
                        res = await axios.patch(url, body);
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