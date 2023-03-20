import React, { useEffect, useState } from "react";
import PageHeader from "src/components/PageHeader/PageHeader";
import { BECOME_SELLER, PROFILE } from "src/constants/endpoints";
import { profileType } from "src/constants/common";
import { useHttpClient } from "src/hooks/http-hook";
import { useNavigate } from "react-router-dom";
import { Button } from "@mui/material";

import style from "./Profile.module.css";

export default function Profile() {
  const { sendRequest } = useHttpClient();
  const [profile, setProfile] = useState<profileType>();
  const [funcSet, setFunc] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const getProfile = async () => {
      let profile: profileType = await sendRequest(PROFILE, "GET", {});
      setProfile(profile);
    };
    getProfile();
  }, [sendRequest]);

  useEffect(() => {
    const becomeSeller = async () => {
      await sendRequest(BECOME_SELLER, "PATCH", {});
      window.location.reload();
    };
    if (funcSet) {
      becomeSeller();
    }
  }, [funcSet, sendRequest]);

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
                  onClick={() => setFunc(true)}
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
