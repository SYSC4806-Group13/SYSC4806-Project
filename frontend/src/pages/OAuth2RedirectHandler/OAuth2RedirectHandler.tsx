import React, { useContext } from "react";
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

  const token = getUrlParameter("token");
  const error = getUrlParameter("error");

  if (token) {
    logIn(token);
    return <>{navigate("/")}</>;
  } else {
    return <>{navigate("/")}</>;
  }
};

export default OAuth2RedirectHandler;
