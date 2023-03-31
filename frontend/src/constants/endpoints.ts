import { httpAuthenticationURL, httpMethod } from "./common";

export const LISTING = "/listings";
export const COVERS = "/covers/";
export const PROFILE = "/users/userProfile";
export const BECOME_SELLER = "/users/becomeSeller";
export const CART_ITEMS = "/cartItems";
export const CART_ITEM = "/cartItem";
export const CHECKOUT = "/checkout";
export const ORDER_HISTORY = "/orderHistory";
export const RECOMMENDATION = "/recommendation"

export const API_BASE_URL = "http://localhost:8080";
export const OAUTH2_REDIRECT_URI = "http://localhost:3000/oauth2/redirect";
export const GOOGLE_AUTH_URL = API_BASE_URL + "/oauth2/authorize/google?redirect_uri=" + OAUTH2_REDIRECT_URI;

export const isAuthenticationNeeded = (type: httpMethod, endPoint: string) => {

  const autheticatedURLS: httpAuthenticationURL = {
    POST: [
      LISTING,
      COVERS,
      CART_ITEMS,
      CHECKOUT,
    ],
    GET: [
      PROFILE,
      CART_ITEMS,
      CART_ITEM,
      ORDER_HISTORY,
      RECOMMENDATION,
    ],
    DELETE: [
      CART_ITEMS,
    ],
    PUT: [
      LISTING,
      CART_ITEMS,
    ],
    PATCH: [
      BECOME_SELLER,
    ],
  };

  for (var i = 0; i < autheticatedURLS[type].length; i++) {
    if (endPoint.startsWith(autheticatedURLS[type][i])) {
      return true;
    }
  }
  return false;
};


