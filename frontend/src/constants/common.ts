export type userValue = {
  isLoggedIn: boolean | null;
  jwtToken: string | undefined;
  profile: profileType;
};

export type userDispatch = {
  logOut: () => void;
  logIn: (token: string) => void;
  setToken: (token: string) => void;
  setProfile: (profile: profileType) => void;
};

export type httpAuthenticationURL = {
  GET: string[];
  POST: string[];
  DELETE: string[];
  PUT: string[];
  PATCH: string[];
};

export type userLoginContextState = userValue & userDispatch;

export type httpMethod = "GET" | "POST" | "DELETE" | "PUT" | "PATCH";

export type profileType = {
  id: string;
  name: string;
  email: string;
  isSeller: string;
};
