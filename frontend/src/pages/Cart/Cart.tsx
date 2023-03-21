import * as React from "react";

import {
    Button,
    IconButton,
    Paper,
    Stack,
    Typography,
} from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import PageHeader from "src/components/PageHeader/PageHeader";
import { useHttpClient } from "src/hooks/http-hook";
import { CART_ITEMS, } from "src/constants/endpoints";
import AddCircleIcon from '@mui/icons-material/AddCircle';
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';
import { useNavigate } from "react-router-dom";

export interface ICartItem {
    quantity: number,
    listing: {
        listingId: number,
        price: number,
        inventory: number,
        title: string,
        author: string,
    }
}

export default function Cart() {
    const [cartItems, setCartItems] = React.useState(Array<ICartItem>);
    const [cartTotalCost, setCartTotalCost] = React.useState(0);
    const { sendRequest } = useHttpClient();
    const navigate = useNavigate();

    // Initial retrieve of cart items
    React.useEffect(() => {
        const getSellerItems = async () => {
            let cartItems = await sendRequest(CART_ITEMS, "GET", {},);

            setCartItems(cartItems);
            let currentCartTotal = 0;
            cartItems.forEach(function (cartItem: ICartItem) {
                currentCartTotal += cartItem.quantity * cartItem.listing.price;
            });
            setCartTotalCost(currentCartTotal);
        };
        getSellerItems();
    }, [sendRequest, cartTotalCost]);

    function deleteCartItem(listingId: number) {
        return async function () {
            try {
                await sendRequest(CART_ITEMS, "DELETE", {
                    listingId: listingId,
                });
                setCartTotalCost(0); // Triggers the refresh via the useEffect which immediately gets reloaded
            } catch (error) {
                console.log(error);
                // TODO: Show an error popup message indicating why the fail happened. Probably needs some Backend updates
            }
        }
    }


    function changeCartQuantity(isAdding: boolean, listingId: number, quantity: number) {
        return async function () {
            const quantityAfterChange = quantity + (isAdding ? 1 : -1);
            try {
                await sendRequest(CART_ITEMS, "PUT", {
                    listingId: listingId,
                    quantity: quantityAfterChange,
                });
                setCartTotalCost(0); // Triggers the refresh via the useEffect which immediately gets reloaded
            } catch (error) {
                console.log(error);
                // TODO: Show an error popup message indicating why the fail happened. Probably needs some Backend updates
            }
        }
    }

    async function checkout() {
        navigate("/simulatedPayment");
    }

    return (
        <PageHeader headerTitle="Cart">
            <>
                <Stack maxWidth="xl" className='grid'>
                    {cartItems.map(cartItem => {
                        cartItem = cartItem as ICartItem;
                        return (
                            <Paper sx={{ margin: 1, padding: 1, backgroundColor: "#cccccc" }}>
                                <Typography variant="h4">
                                    {cartItem.listing.title}
                                </Typography>
                                <Typography variant="h6">
                                    Author: {cartItem.listing.author}
                                </Typography>
                                <Typography variant="h5">
                                    Price: ${cartItem.listing.price}
                                </Typography>
                                <Typography>
                                    Inventory: {cartItem.listing.inventory}
                                </Typography>
                                <Typography>
                                    Quantity: {cartItem.quantity}
                                </Typography>

                                <IconButton onClick={deleteCartItem(cartItem.listing.listingId)}>
                                    <DeleteIcon fontSize='large' />
                                </IconButton>
                                <IconButton onClick={changeCartQuantity(false, cartItem.listing.listingId, cartItem.quantity)} disabled={cartItem.quantity === 1}>
                                    <RemoveCircleIcon fontSize='large' />
                                </IconButton>
                                <IconButton onClick={changeCartQuantity(true, cartItem.listing.listingId, cartItem.quantity)} disabled={cartItem.quantity === cartItem.listing.inventory}>
                                    <AddCircleIcon fontSize='large' />
                                </IconButton>
                            </Paper>
                        )
                    })}
                </Stack>

                {cartItems.length <= 0 &&
                    <Typography>Cart is empty</Typography>
                }
                {cartItems.length > 0 &&
                    <>
                        <Typography>Cart Subtotal: ${cartTotalCost}</Typography>
                        <Button
                            variant="contained"
                            color="success"
                            size="large"
                            onClick={checkout}
                        >
                            Checkout with simulated payment
                        </Button>
                    </>
                }
            </>
        </PageHeader>
    );
}