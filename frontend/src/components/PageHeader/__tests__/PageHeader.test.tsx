import React from "react";
import { render, screen } from "@testing-library/react";
import { UserLoginContext } from "src/context/userLoginContext";
import PageHeader from "src/components/PageHeader/PageHeader";

const mockedUsedNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
  ...(jest.requireActual("react-router-dom") as any),
  useNavigate: () => mockedUsedNavigate,
}));

describe("Testing PageHeader", () => {
  it("renders children", () => {
    render(
      <PageHeader headerTitle="Test">
        <p>Test Stuff</p>
      </PageHeader>
    );
    const pageContent = screen.getByText(/Test Stuff/i);

    expect(pageContent).toBeInTheDocument();
  });

  it("renders the header logged in", () => {
    render(
      <UserLoginContext.Provider
        value={{
          isLoggedIn: true,
          jwtToken: "state.jwtToken",
          profile: { name: "kevin", id: "1", email: "email", isSeller: "true" },
          logOut: () => {},
          logIn: () => {},
          setToken: () => {},
          setProfile: () => {},
        }}
      >
        <PageHeader headerTitle="Test">
          <p>Test Stuff</p>
        </PageHeader>
      </UserLoginContext.Provider>
    );

    const loginButton = screen.queryAllByTestId("login button");
    expect(loginButton).toHaveLength(0);
  });

  it("renders the header logged out", () => {
    render(
      <UserLoginContext.Provider
        value={{
          isLoggedIn: false,
          jwtToken: "state.jwtToken",
          profile: { name: "kevin", id: "1", email: "email", isSeller: "true" },
          logOut: () => {},
          logIn: () => {},
          setToken: () => {},
          setProfile: () => {},
        }}
      >
        <PageHeader headerTitle="Test">
          <p>Test Stuff</p>
        </PageHeader>
      </UserLoginContext.Provider>
    );

    const loginButton = screen.getByTestId("login button");
    expect(loginButton).toBeInTheDocument();
  });
});
