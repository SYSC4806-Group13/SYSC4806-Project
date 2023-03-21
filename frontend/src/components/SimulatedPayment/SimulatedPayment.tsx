import * as React from "react";
import { useHttpClient } from "src/hooks/http-hook";
import {
    Button,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { CART_ITEMS, CHECKOUT } from "src/constants/endpoints";
import PageHeader from "../PageHeader/PageHeader";


export default function SimulatedPayment() {
    const { sendRequest } = useHttpClient();
    const navigate = useNavigate();
    const [cartSize, setCartSize] = React.useState(0);

    React.useEffect(() => {
        const getSellerItems = async () => {
            let cartItems = await sendRequest(CART_ITEMS, "GET", {},);
            setCartSize(cartItems.length);
        };
        getSellerItems();
    }, [sendRequest, cartSize]);

    async function pay() {
        await sendRequest(CHECKOUT, "POST", {});
        navigate("/orderHistory");
    }

    return (
        <PageHeader headerTitle="Payment">
            <>
                {cartSize !== 0 &&
                    <>
                        <Typography>Imagine that you just entered your billing details and shipping information</Typography>
                        <Button
                            variant="contained"
                            color="success"
                            size="large"
                            onClick={pay}
                        >
                            Confirm Payment
                        </Button>
                    </>
                }
                {cartSize === 0 &&
                    <>
                        <Typography>Error: Cannot pay with an empty cart</Typography>
                    </>
                }
            </>
        </PageHeader>
    );
}