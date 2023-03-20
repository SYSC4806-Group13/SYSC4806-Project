export type userValue = {
  isLoggedIn: boolean | null;
  jwtToken: string | undefined;
};

export type userDispatch = {
  logOut: () => void;
  logIn: (token: string) => void;
  setToken: (token: string) => void;
};

export type userLoginContextState = userValue & userDispatch;

export type httpAuthenticationURL = {
  GET: String[];
  POST: String[];
  DELETE: String[];
  PUT: String[];
  PATCH: String[];
};

export type httpMethod = "GET" | "POST" | "DELETE" | "PUT" | "PATCH";

export type profileType = {
  id: string;
  name: string;
  email: string;
  isSeller: string;
};
