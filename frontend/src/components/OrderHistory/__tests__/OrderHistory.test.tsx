import { render, screen } from "@testing-library/react";
import { UserLoginContext } from "src/context/userLoginContext";
import OrderHistory from "../OrderHistory";

const mockedUsedNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
    ...(jest.requireActual("react-router-dom") as any),
    useNavigate: () => mockedUsedNavigate,
  }));
  

describe("Testing PageHeader", () => {
    it("renders", () => {
        render(
                <UserLoginContext.Provider
                    value={{
                        isLoggedIn: true,
                        jwtToken: "state.jwtToken",
                        logOut: () => { },
                        logIn: () => { },
                        setToken: () => { },
                    }}
                >
                    <OrderHistory />
                </UserLoginContext.Provider>
        );
        const pageContent = screen.getByText("No orders on record");

        expect(pageContent).toBeInTheDocument();
    });
})