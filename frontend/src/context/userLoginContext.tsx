import React, {
  createContext,
  useReducer,
  ReactNode,
  useEffect,
  useState,
} from "react";

import { profileType, userLoginContextState } from "src/constants/common";
import { reducer } from "./userLoginState";

const isLoggedLS = localStorage.getItem("isLoggedIn");

const initialState = {
  isLoggedIn: isLoggedLS ? true : false,
  jwtToken: "",
  profile: { id: "", name: "", email: "", isSeller: "false" } as profileType,
  logOut: () => {
    /** */
  },
  logIn: (token: string) => {
    /** */
  },
  setToken: (token: string) => {
    /** */
  },
  setProfile: (profile: profileType) => {
    /** */
  },
};

export const UserLoginContext =
  createContext<userLoginContextState>(initialState);

export function UserLoginProvider({ children }: { children: ReactNode }) {
  const [state, dispatch] = useReducer(reducer, initialState);
  const [profile, setNewProfile] = useState<profileType>(initialState.profile);

  const logOut = () => {
    localStorage.removeItem("amazin_jwt");
    localStorage.removeItem("amazin_user");
    dispatch({
      type: "LOGOUT",
    });
  };

  useEffect(() => {
    const getToken = localStorage.getItem("amazin_jwt") || "";
    const getUser = localStorage.getItem("amazin_user") || "";
    if (getToken !== "" && getUser !== "") {
      setProfile(JSON.parse(getUser) as profileType);
      dispatch({
        type: "LOGIN",
        payload: { isLoggedIn: true },
      });
    }
  }, []);

  const setToken = (token: string) => {
    localStorage.setItem("amazin_jwt", token);
  };

  const setProfile = (profile: profileType) => {
    localStorage.setItem("amazin_user", JSON.stringify(profile));
    setNewProfile(profile);
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
        profile: profile,
        logOut,
        logIn,
        setToken,
        setProfile,
      }}
    >
      {children}
    </UserLoginContext.Provider>
  );
}
