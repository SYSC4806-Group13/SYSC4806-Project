import * as React from "react";
import PageHeader from "src/components/PageHeader/PageHeader";
import {
  Paper,
  Typography,
  Stack,
} from "@mui/material";
import { useHttpClient } from "src/hooks/http-hook";
import { ORDER_HISTORY } from "src/constants/endpoints";

interface IOrderHistoryItem {
  purchaseDateTime: string,
  totalCartItemPriceAtPurchase: number,
  quantity: number,
  listing: {
    title: string,
    author: string,
  }
}

export default function OrderHistory() {
  const [orderHistoryItems, setOrderHistoryItems] = React.useState(Array<IOrderHistoryItem>);
  const { sendRequest } = useHttpClient();

  // Initial retrieve of cart items
  React.useEffect(() => {
    const getSellerItems = async () => {
      let response = await sendRequest(ORDER_HISTORY, "GET", {},);
      setOrderHistoryItems(response);
    };
    getSellerItems();
  }, [sendRequest,]);

  return (
    <PageHeader headerTitle="Order History">
      <>
        {orderHistoryItems.length !== 0 &&
          <Stack maxWidth="xl" className='grid'>
            {orderHistoryItems.map(orderHistoryItem => {
              const purchaseDateTime = new Date(orderHistoryItem.purchaseDateTime);
              return (
                <Paper sx={{ margin: 1, padding: 1, backgroundColor: "#cccccc" }}>
                  <Typography variant="h4">
                    {orderHistoryItem.listing.title}
                  </Typography>
                  <Typography variant="h6">
                    Author: {orderHistoryItem.listing.author}
                  </Typography>
                  <Typography variant="h6">
                    Quantity: {orderHistoryItem.quantity}
                  </Typography>
                  <Typography variant="h6">
                    Total Price: ${orderHistoryItem.totalCartItemPriceAtPurchase}
                  </Typography>
                  <Typography variant="h6">
                    Purchased on: {purchaseDateTime.toLocaleDateString()} at {purchaseDateTime.toLocaleTimeString()}
                  </Typography>
                </Paper>
              );
            })}
          </Stack>
        }
        {orderHistoryItems.length === 0 &&
          <Typography variant="h4">No orders on record</Typography>
        }
      </>
    </PageHeader>
  );
}