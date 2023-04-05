import React, { useContext } from "react";
import PageHeader from "src/components/PageHeader/PageHeader";
import { BECOME_SELLER, PROFILE } from "src/constants/endpoints";
import { useHttpClient } from "src/hooks/http-hook";
import { useNavigate } from "react-router-dom";
import { Button } from "@mui/material";

import style from "./Profile.module.css";
import { UserLoginContext } from "src/context/userLoginContext";
import { profileType } from "src/constants/common";

export default function Profile() {
  const { sendRequest } = useHttpClient();

  const navigate = useNavigate();
  const { profile, setProfile } = useContext(UserLoginContext);

  const becomeSeller = async () => {
    await sendRequest(BECOME_SELLER, "PATCH", {});
    getProfile();
  };

  const getProfile = async () => {
    let profile1: profileType = await sendRequest(PROFILE, "GET", {});
    setProfile(profile1);
  };

  return (
    <PageHeader headerTitle="Profile">
      <div className={style.container}>
        <div className={style.box}>
          <p>Profile:{profile?.id && profile.id}</p>
          <p>Name:{profile?.name && profile.name}</p>
          <p>Email:{profile?.email && profile.email}</p>
          <p>Is A Seller: {profile?.isSeller && profile.isSeller}</p>
          <>
            {profile?.isSeller !== undefined &&
              (profile.isSeller === "false" ? (
                <Button
                  variant="contained"
                  color="success"
                  size="large"
                  className={style.button}
                  onClick={() => becomeSeller()}
                >
                  Become a Seller
                </Button>
              ) : (
                <Button
                  variant="contained"
                  color="success"
                  size="large"
                  className={style.button}
                  onClick={() => navigate("/seller/" + profile?.id)}
                >
                  Sellers Page
                </Button>
              ))}
          </>
        </div>
      </div>
    </PageHeader>
  );
}
