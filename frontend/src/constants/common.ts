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
    "GET": string[],
    "POST": string[],
    "DELETE": string[],
    "PUT": string[],
    "PATCH" : string[],
  }

export type userLoginContextState = userValue & userDispatch;

export type httpMethod = "GET" | "POST" | "DELETE" | "PUT" | "PATCH";

export type profileType = {
  id: string;
  name: string;
  email: string;
  isSeller: string;
};

export interface IListingCardProps {
  cardName: string,
  author: string,
  title: string,
  price: string,
  image: string,
  alt: string,
  carousel?: boolean,
  listingId: string,
  isbn: string,
  publisher: string,
  description: string,
  inventory: string,
  releaseDate: string,
}
