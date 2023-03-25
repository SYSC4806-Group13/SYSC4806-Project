import { userValue } from "src/constants/common";

const isLoggedLS = localStorage.getItem("amazin_jwt");

export const initialState = {
  isLoggedIn: isLoggedLS ? true : false,
  jwtToken: "",
};

type LogIn = {
  type: "LOGIN";
  payload: any;
};

type LogOut = {
  type: "LOGOUT";
};

type AddProfile = {
  type: "ADDPROFILE";
};

export type ValidAction = LogIn | LogOut | AddProfile;

export const reducer = (state: userValue, action: ValidAction): userValue => {
  switch (action.type) {
    case "LOGIN": {
      return {
        ...state,
        isLoggedIn: true,
      };
    }
    case "LOGOUT": {
      return {
        ...state,
        isLoggedIn: false,
      };
    }
    default:
      return state;
  }
};
