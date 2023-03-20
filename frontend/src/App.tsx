import React, { useContext } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import AllListings from "src/pages/AllListings";
import SellerListings from "src/pages/SellerListings";
import { UserLoginContext } from "src/context/userLoginContext";
import ProtectedPage from "./pages/ProtectedPage/ProtectedPage";
import UnAuthorizedPage from "./pages/UnAuthorizedPage/UnAuthorizedPage";
import OAuth2RedirectHandler from "./pages/OAuth2RedirectHandler/OAuth2RedirectHandler";
import Profile from "./pages/Profile/Profile";
import Login from "./pages/Login/Login";

export default function App() {
  const { isLoggedIn } = useContext(UserLoginContext);

  const isLoggedInRouteAccess = (
    isLoggedInRoute: JSX.Element,
    isNotLoggedInRoute: JSX.Element
  ) => {
    return isLoggedIn ? isLoggedInRoute : isNotLoggedInRoute;
  };

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AllListings />} />
        <Route
          path="/seller/:sellerId"
          element={isLoggedInRouteAccess(
            <SellerListings />,
            <UnAuthorizedPage />
          )}
        />
        <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
        <Route
          path="/protected"
          element={isLoggedInRouteAccess(
            <ProtectedPage />,
            <UnAuthorizedPage />
          )}
        />
        <Route
          path="/profile"
          element={isLoggedInRouteAccess(<Profile />, <UnAuthorizedPage />)}
        />
        <Route path="/login" element={<Login />} />
      </Routes>
    </BrowserRouter>
  );
}
