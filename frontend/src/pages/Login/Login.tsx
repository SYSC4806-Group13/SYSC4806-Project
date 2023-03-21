import React from "react";
import GoogleButton from "react-google-button";
import PageHeader from "src/components/PageHeader/PageHeader";
import { GOOGLE_AUTH_URL } from "src/constants/endpoints";
import style from "./Login.module.css";

export default function Login() {
  return (
    <>
      <PageHeader headerTitle="Login">
        <div className={style.box}>
          <p>Log into amazin with Google</p>
          <GoogleButton
            onClick={() => (window.location.href = GOOGLE_AUTH_URL)}
          ></GoogleButton>
        </div>
      </PageHeader>
    </>
  );
}
