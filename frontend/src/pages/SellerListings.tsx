import * as React from "react";
import { Button, Box } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import ListingGrid from "src/components/Listing/ListingGrid";
import PageHeader from "src/components/PageHeader/PageHeader";
import DialogBox from "src/components/Dialog/DialogBox";
import SellerListingForm from "src/components/Seller/SellerListingForm";
import "src/styles/SellerListing.css";

export interface ISellerListingsProps {}

export default function SellerListings(props: ISellerListingsProps) {
  const listing = [
    {
      cardName: "test",
      author: "test",
      price: "12.22",
      image: "/static/images/lizard.jpg",
      alt: "lizard",
    },
    {
      cardName: "test",
      author: "test",
      price: "12.22",
      image: "/static/images/lizard.jpg",
      alt: "lizard",
    },
    {
      cardName: "test",
      author: "test",
      price: "12.22",
      image: "/static/images/book-cover.jpg",
      alt: "book",
    },
    {
      cardName: "test",
      author: "test",
      price: "12.22",
      image: "/static/images/book-cover.jpg",
      alt: "book",
    },
    {
      cardName: "test",
      author: "test",
      price: "12.22",
      image: "/static/images/book-cover.jpg",
      alt: "book",
    },
  ];
  const [addListingDialog, setaddListingDialog] = React.useState(false);
  const handleAddListing = () => {
    setaddListingDialog(true);
  };
  const closeDialog = () => setaddListingDialog(false);
  return (
    <PageHeader headerTitle="Seller Listings">
      <Box textAlign={"center"} mt={2}>
        <Button
          variant="contained"
          color="success"
          size="large"
          className="button"
          onClick={handleAddListing}
        >
          Add Listing <AddIcon fontSize="large" />
        </Button>
      </Box>
      <ListingGrid listings={listing} />
      <DialogBox
        isDialogOpen={addListingDialog}
        handleCloseDialog={closeDialog}
      >
        <SellerListingForm handleCloseDialog={closeDialog} />
      </DialogBox>
    </PageHeader>
  );
}
