import * as React from 'react';

import Typography from '@mui/material/Typography';
import { Button, IconButton } from '@mui/material';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import 'src/styles/ListingCard.css'
import { CART_ITEM, CART_ITEMS } from "src/constants/endpoints";
import { useHttpClient } from "src/hooks/http-hook";
import AddCircleIcon from '@mui/icons-material/AddCircle';
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';
import { UserLoginContext } from 'src/context/userLoginContext';

export interface IAddToCartButtonProps {
  listingId: string,
}

export default function AddToCartButton({ listingId }: IAddToCartButtonProps) {
  const { isLoggedIn } = React.useContext(UserLoginContext);
  const [quantityInCart, setQuantityInCart,] = React.useState(0);
  const [inventory, setInventory,] = React.useState(10);
  const { sendRequest } = useHttpClient();

  // Initial retrieve
  React.useEffect(() => {
    const getSellerItems = async () => {
      let currentCartItem = await sendRequest(
        CART_ITEM,
        "GET",
        {},
        `?listingId=${listingId}`
      );
      setQuantityInCart(currentCartItem.quantity);
      setInventory(currentCartItem.inventory);
    };
    getSellerItems();
  }, [sendRequest, listingId]);

  function changeCartQuantity(isAdding: boolean) {
    return async function () {
      const quantityAfterChange = quantityInCart + (isAdding ? 1 : -1);
      const requestMethod = quantityAfterChange === 0 ? "DELETE" : (quantityInCart === 0 && quantityAfterChange === 1) ? "POST" : "PUT";
      try {
        const response = await sendRequest(CART_ITEMS, requestMethod, {
          listingId: listingId,
          quantity: quantityAfterChange,
        });
        if (requestMethod === "DELETE") {
          setInventory(response.inventory);
          setQuantityInCart(0);
        } else {
          setQuantityInCart(response.quantity);
          setInventory(response.listing.inventory);
        }
      } catch (error) {
        console.log(error);
        // TODO: Show an error popup message indicating why the fail happened. Probably needs some Backend updates
      }
    }
  }

  return (
    <>
      {isLoggedIn &&
        <>
          {inventory !== 0 && quantityInCart <= 0 &&
            <>
              <Button size="large" color="success" onClick={changeCartQuantity(true)}>
                <Typography variant="h6">
                  Add to Cart
                </Typography>
                <AddShoppingCartIcon className='icon-spacing' />
              </Button>
            </>
          }
          {inventory !== 0 && quantityInCart > 0 &&
            <>
              <Typography variant="h6" color="success">
                Cart Quantity: {quantityInCart}
              </Typography>


              <IconButton onClick={changeCartQuantity(false)}>
                <RemoveCircleIcon fontSize='large' />
              </IconButton>
              <IconButton onClick={changeCartQuantity(true)} disabled={quantityInCart === inventory}>
                <AddCircleIcon fontSize='large' />
              </IconButton>
            </>
          }
          {inventory === 0 &&
            <>
              <Typography>Out of stock</Typography>
            </>
          }
        </>
      }
    </>
  );
}