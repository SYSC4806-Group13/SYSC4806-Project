import React, { useContext } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import AllListings from "src/pages/AllListings";
import SellerListings from "src/pages/SellerListings";
import { UserLoginContext } from "src/context/userLoginContext";
import ProtectedPage from "./pages/ProtectedPage/ProtectedPage";
import UnAuthorizedPage from "./pages/UnAuthorizedPage/UnAuthorizedPage";

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
        <Route path="/seller/:sellerId" element={<SellerListings />} />
        <Route
          path="/protected"
          element={isLoggedInRouteAccess(
            <ProtectedPage />,
            <UnAuthorizedPage />
          )}
        />
      </Routes>
    </BrowserRouter>
  );
}
