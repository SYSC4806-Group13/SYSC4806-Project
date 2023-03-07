import React from "react";
import { render, screen } from "@testing-library/react";
import UnAuthorizedPage from "../UnAuthorizedPage";
import { UserLoginContext } from "src/context/userLoginContext";

const mockedUsedNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
  ...(jest.requireActual("react-router-dom") as any),
  useNavigate: () => mockedUsedNavigate,
}));

describe("Testing UnAuthorizedPage", () => {
  it("renders the page", () => {
    render(<UnAuthorizedPage />);
    const headerElement = screen.getByText(
      /This is a sample of an logged out page/i
    );
    const paragraphElement = screen.getByText(
      /If you see this, you are not loggedin/i
    );
    expect(headerElement).toBeInTheDocument();
    expect(paragraphElement).toBeInTheDocument();
  });

  it("renders the page with header logged in", () => {
    render(
      <UserLoginContext.Provider
        value={{
          isLoggedIn: true,
          jwtToken: "state.jwtToken",
          logOut: () => {},
          logIn: () => {},
          setToken: () => {},
        }}
      >
        <UnAuthorizedPage />
      </UserLoginContext.Provider>
    );

    const loginButton = screen.queryAllByTestId("login button");
    expect(loginButton).toHaveLength(0);
  });

  it("renders the page with header logged out", () => {
    render(
      <UserLoginContext.Provider
        value={{
          isLoggedIn: false,
          jwtToken: "state.jwtToken",
          logOut: () => {},
          logIn: () => {},
          setToken: () => {},
        }}
      >
        <UnAuthorizedPage />
      </UserLoginContext.Provider>
    );

    const loginButton = screen.getByTestId("login button");
    expect(loginButton).toBeInTheDocument();
  });
});
