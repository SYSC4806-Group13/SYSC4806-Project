import { render, screen } from "@testing-library/react";
import { UserLoginContext } from "src/context/userLoginContext";
import Cart from "../Cart";


const mockedUsedNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
    ...(jest.requireActual("react-router-dom") as any),
    useNavigate: () => mockedUsedNavigate,
}));


describe("Testing SimulatedPayment", () => {
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
                <Cart />
            </UserLoginContext.Provider>
        );
        const pageContent = screen.getByText("Cart is empty");

        expect(pageContent).toBeInTheDocument();
    });
});