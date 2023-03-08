import React, { createContext, useReducer, ReactNode, useEffect } from "react";

import { userLoginContextState } from "../constants/common";
import { reducer } from "./userLoginState";

const isLoggedLS = localStorage.getItem("isLoggedIn");

const initialState = {
  isLoggedIn: isLoggedLS ? true : false,
  jwtToken: "",
  logOut: () => {
    /** */
  },
  logIn: (token: string) => {
    /** */
  },
  setToken: (token: string) => {
    /** */
  },
};

export const UserLoginContext =
  createContext<userLoginContextState>(initialState);

export function UserLoginProvider({ children }: { children: ReactNode }) {
  const [state, dispatch] = useReducer(reducer, initialState);

  const logOut = () => {
    localStorage.removeItem("amazin_jwt");
    dispatch({
      type: "LOGOUT",
    });
  };

  useEffect(() => {
    const getToken = localStorage.getItem("amazin_jwt") || "";
    if (getToken !== "") {
      dispatch({
        type: "LOGIN",
        payload: { isLoggedIn: true },
      });
    }
  }, []);

  const setToken = (token: string) => {
    localStorage.setItem("amazin_jwt", token);
  };

  const logIn = (token: string) => {
    setToken(token);
    dispatch({
      type: "LOGIN",
      payload: { isLoggedIn: true, jwtToken: token },
    });
  };

  return (
    <UserLoginContext.Provider
      value={{
        isLoggedIn: state.isLoggedIn,
        jwtToken: state.jwtToken,
        logOut,
        logIn,
        setToken,
      }}
    >
      {children}
    </UserLoginContext.Provider>
  );
}
