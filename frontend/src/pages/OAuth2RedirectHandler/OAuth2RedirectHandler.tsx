import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { PROFILE } from "src/constants/endpoints";
import { UserLoginContext } from "src/context/userLoginContext";
import { useHttpClient } from "src/hooks/http-hook";

const OAuth2RedirectHandler = () => {
  const navigate = useNavigate();
  const { logIn, setProfile, profile } = useContext(UserLoginContext);
  const { sendRequest } = useHttpClient();
  const [done, setDone] = useState(false);

  function getUrlParameter(name: string) {
    name = name.replace(/[[]/, "\\[").replace(/[\]]/, "]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");

    var results = regex.exec(window.location.search);
    return results === null
      ? ""
      : decodeURIComponent(results[1].replace(/\+/g, " "));
  }

  useEffect(() => {
    if (profile.name === "" && !done) {
      const token = getUrlParameter("token");
      if (token) {
        logIn(token);
        const getProfile = async () => {
          const profile = await sendRequest(PROFILE, "GET", {});
          setProfile(profile);
          setDone(true);
        };
        getProfile();
      } else {
        navigate("/login");
      }
    }
  }, [profile, logIn, navigate, setProfile, sendRequest, done, setDone]);

  useEffect(() => {
    if (done) navigate("/");
  }, [profile, navigate, done]);

  return <></>;
};

export default OAuth2RedirectHandler;
