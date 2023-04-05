import React, { useContext } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AllListings from "src/pages/AllListings";
import SellerListings from "src/pages/SellerListings";
import { UserLoginContext } from "src/context/userLoginContext";
import ProtectedPage from "src/pages/ProtectedPage/ProtectedPage";
import UnAuthorizedPage from "src/pages/UnAuthorizedPage/UnAuthorizedPage";
import OAuth2RedirectHandler from "src/pages/OAuth2RedirectHandler/OAuth2RedirectHandler";
import Profile from "src/pages/Profile/Profile";
import Login from "src/pages/Login/Login";
import Cart from "src/pages/Cart/Cart";
import OrderHistory from "src/components/OrderHistory/OrderHistory";
import SimulatedPayment from "src/components/SimulatedPayment/SimulatedPayment";
import Listing from "./pages/Listing/Listing";

export default function App() {
  const { isLoggedIn, profile } = useContext(UserLoginContext);

  const isSellerRouteAccess = () => {
    return profile.isSeller;
  };

  const isLoggedInRedirectHome = () => {
    if (isLoggedIn) {
      return <Navigate to="/" />;
    }
    return <Login />;
  };

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
            isSellerRouteAccess() ? <SellerListings /> : <UnAuthorizedPage />,
            <Login />
          )}
        />
        <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/orderHistory" element={<OrderHistory />} />
        <Route path="/simulatedPayment" element={<SimulatedPayment />} />
        <Route path="/listing/*" element={<Listing />} />
        <Route
          path="/protected"
          element={isLoggedInRouteAccess(
            <ProtectedPage />,
            <UnAuthorizedPage />
          )}
        />
        <Route
          path="/profile"
          element={isLoggedInRouteAccess(<Profile />, <Login />)}
        />
        <Route path="/login" element={isLoggedInRedirectHome()} />
      </Routes>
    </BrowserRouter>
  );
}
