import * as React from "react";
import { useParams } from "react-router-dom";
import { Button, Box } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import ListingGrid from "src/components/Listing/ListingGrid";
import PageHeader from "src/components/PageHeader/PageHeader";
import DialogBox from "src/components/Dialog/DialogBox";
import SellerListingForm from "src/components/Seller/SellerListingForm";
import { useHttpClient } from "src/hooks/http-hook";
import { buildListings } from "src/utils/listings";
import { UserLoginContext } from "src/context/userLoginContext";
import "src/styles/SellerListing.css";
import { LISTING } from "src/constants/endpoints";

export interface ISellerListingsProps {}

export default function SellerListings(props: ISellerListingsProps) {
  const [addListingDialog, setaddListingDialog] = React.useState(false);
  const handleAddListing = () => {
    setaddListingDialog(true);
  };
  const closeDialog = () => setaddListingDialog(false);
  let { sellerId } = useParams();

  const [sellerItems, setSellerItems] = React.useState([]);
  const { sendRequest } = useHttpClient();
  const { isLoggedIn } = React.useContext(UserLoginContext);

  React.useEffect(() => {
    const getSellerItems = async () => {
      let items = await sendRequest(LISTING, "GET", {});
      items = buildListings(items);
      setSellerItems(items);
    };
    getSellerItems();
  }, [sendRequest, addListingDialog]);

  return (
    <PageHeader headerTitle="Seller Listings">
      <>
        {isLoggedIn && (
          <>
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
            <DialogBox
              isDialogOpen={addListingDialog}
              handleCloseDialog={closeDialog}
            >
              <SellerListingForm
                handleCloseDialog={closeDialog}
                sellerId={sellerId}
              />
            </DialogBox>
          </>
        )}
      </>

      <ListingGrid listings={sellerItems} />
    </PageHeader>
  );
}
