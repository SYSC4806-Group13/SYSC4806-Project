export type userValue = {
    isLoggedIn: boolean | null
    jwtToken: string | undefined
  }
  
  export type userDispatch = {
    logOut: () => void
    logIn: (token: string) => void
    setToken: (token: string) => void
  }

  export type userLoginContextState = userValue & userDispatch

  export type httpAuthenticationURL = {
    "GET": string[],
    "POST": string[],
    "DELETE": string[],
    "PUT": string[],
    "PATCH" : string[],
  }

  export type httpMethod = "GET" | "POST" | "DELETE" | "PUT" | "PATCH"