import React, { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { UserLoginContext } from "src/context/userLoginContext";

const OAuth2RedirectHandler = () => {
  const navigate = useNavigate();
  const { logIn } = useContext(UserLoginContext);

  function getUrlParameter(name: string) {
    name = name.replace(/[[]/, "\\[").replace(/[\]]/, "]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");

    var results = regex.exec(window.location.search);
    return results === null
      ? ""
      : decodeURIComponent(results[1].replace(/\+/g, " "));
  }

  useEffect(() => {
    const token = getUrlParameter("token");
    if (token) {
      logIn(token);
      navigate("/");
    } else {
      navigate("/login");
    }
  }, [logIn, navigate]);

  return <></>;
};

export default OAuth2RedirectHandler;
