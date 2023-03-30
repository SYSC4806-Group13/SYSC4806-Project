import * as React from "react";
import PageHeader from "src/components/PageHeader/PageHeader";
import {
  Box,
  Paper,
  Typography,
  Stack,
} from "@mui/material";
import { useHttpClient } from "src/hooks/http-hook";
import { API_BASE_URL, COVERS, ORDER_HISTORY } from "src/constants/endpoints";

interface IOrderHistoryItem {
  purchaseDateTime: string,
  totalCartItemPriceAtPurchase: number,
  quantity: number,
  listing: {
    title: string,
    author: string,
    listingId: number,
  }
}

const IMAGE_DIMENSIONS = 150;
const ORDER_HISTORY_ITEM_PAPER_STYLE = {
  margin: 1,
  padding: 3,
  backgroundColor: "#cccccc",
  display: 'flex',
};

export default function OrderHistory() {
  const [orderHistoryItems, setOrderHistoryItems] = React.useState(Array<IOrderHistoryItem>);
  const { sendRequest } = useHttpClient();

  // Initial retrieve of cart items
  React.useEffect(() => {
    const getSellerItems = async () => {
      let response = await sendRequest(ORDER_HISTORY, "GET", {},);
      setOrderHistoryItems(response);
      console.log(response);
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
                <Paper
                  sx={ORDER_HISTORY_ITEM_PAPER_STYLE}
                  elevation={24}
                >
                  <Box sx={{ marginRight: 3 }}>
                    <img
                      src={API_BASE_URL + COVERS + orderHistoryItem.listing.listingId}
                      alt=""
                      height={IMAGE_DIMENSIONS}
                      width={IMAGE_DIMENSIONS}
                    />
                  </Box>
                  <Box sx={{ width: 2, backgroundColor: "#000000", marginRight: 2 }} />{/* Vertical divider line */}
                  <Box>
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
                  </Box>
                </Paper>
              );
            })}
          </Stack>
        }
        {orderHistoryItems.length === 0 &&
          <Typography variant="h4" sx={{ margin: 2 }}>No orders on record</Typography>
        }
      </>
    </PageHeader>
  );
}