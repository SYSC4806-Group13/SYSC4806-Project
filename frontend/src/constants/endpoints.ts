import { httpAuthenticationURL, httpMethod } from "./common";

export const LISTING = "/listings";
export const COVERS = "/covers/";
export const PROFILE = "/users/userProfile";
export const BECOME_SELLER = "/users/becomeSeller";

export const API_BASE_URL = "http://localhost:8080";
export const OAUTH2_REDIRECT_URI = "http://localhost:3000/oauth2/redirect";
export const GOOGLE_AUTH_URL =
  API_BASE_URL + "/oauth2/authorize/google?redirect_uri=" + OAUTH2_REDIRECT_URI;

export const isAuthenticationNeeded = (type: httpMethod, endPoint: string) => {
  const autheticatedURLS: httpAuthenticationURL = {
    POST: [LISTING, COVERS],
    GET: [PROFILE],
    DELETE: [],
    PUT: [],
    PATCH: [BECOME_SELLER],
  };

  for (var i = 0; i < autheticatedURLS[type].length; i++) {
    if (endPoint.startsWith(autheticatedURLS[type][i])) {
      return true;
    }
  }
  return false;
};
/**  const autheticatedURLS: httpAuthenticationURL = {
    POST: [LISTING],
    GET: [PROFILE],
    DELETE: [],
    PUT: [],
    PATCH: [BECOME_SELLER],
  };
  return autheticatedURLS[type].includes(endPoint);*/
