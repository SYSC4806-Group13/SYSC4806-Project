import { httpAuthenticationURL, httpMethod } from "./common";

export const LISTING = "/listings"

export const API_BASE_URL = "http://localhost:8080";
export const OAUTH2_REDIRECT_URI = "http://localhost:3000/oauth2/redirect"
export const GOOGLE_AUTH_URL = API_BASE_URL + "/oauth2/authorize/google?redirect_uri=" + OAUTH2_REDIRECT_URI;

export const isAuthenticationNeeded = (type: httpMethod, endPoint: string) => {
    const autheticatedURLS:httpAuthenticationURL = {"POST":[LISTING], "GET":[], "DELETE":[], "PUT":[], "PATCH":[]}
    return autheticatedURLS[type].includes(endPoint)
}