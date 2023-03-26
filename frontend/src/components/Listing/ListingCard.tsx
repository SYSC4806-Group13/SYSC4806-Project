import * as React from 'react';
import { useHttpClient } from 'src/hooks/http-hook';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { Button, CardActionArea, CardActions } from '@mui/material';
import { capitalize } from '@mui/material/utils';
import AddEditIcon from '@mui/icons-material/Edit';
import 'src/styles/ListingCard.css'
import { useParams } from "react-router-dom";
import DialogBox from "src/components/Dialog/DialogBox";
import SellerListingForm from "src/components/Seller/SellerListingForm";
import { useContext } from "react";
import { UserLoginContext } from "src/context/userLoginContext";
import { useLocation } from 'react-router-dom'
import 'src/styles/ListingCard.css'
import AddToCartButton from 'src/components/Listing/AddToCartButton';

export interface IListingCardProps {
  cardName: string,
  author: string,
  title: string,
  price: string,
  image: string,
  alt: string,
  carousel?: boolean,
  listingId: string,
  isbn: string,
  publisher: string,
  description: string,
  inventory: string,
  releaseDate: string,
}

export default function ListingCard(props: IListingCardProps) {
  const [editListingDialog, setEditListingDialog] = React.useState(false);
  const { isLoggedIn } = useContext(UserLoginContext);
  const location = useLocation();
  const { host } = useHttpClient();
  const handleEditListing = () => {
    setEditListingDialog(true);

  };

  let { sellerId } = useParams();
  const closeDialog = () => setEditListingDialog(false);

  const previousListingFormValues = {
    isbn: props.isbn,
    title: props.title,
    author: props.author,
    publisher: props.publisher,
    description: props.description,
    inventory: parseInt(props.inventory),
    price: parseFloat(props.price),
    releaseDate: props.releaseDate,
    listingId: props.listingId
  };


  return (
    <Card>
      <CardActionArea>
        <CardMedia
          component="img"
          src={host + '/covers/' + props.listingId}
          height="300"
          alt={props.alt}
          className="image"
        />
        {!props.carousel && <CardContent>
          <Typography variant="h3">
            {capitalize(props.cardName)}
          </Typography>
          <Typography gutterBottom variant="h5" color="text.secondary">
            {props.author}
          </Typography>
          <Typography variant="h4" color="text.secondary">
            ${props.price}
          </Typography>
        </CardContent> }
      </CardActionArea>

      <CardActions className='align-center'>
        {isLoggedIn && !location.pathname.includes("/seller/") &&
          <AddToCartButton listingId={props.listingId} />
        }

        {isLoggedIn && location.pathname.includes("/seller/") &&
          //TODO for later, make local with seller. I.E. each seller can only edit their books, not all of them
          <Button
            variant="contained"
            color="primary"
            size="medium"
            className="button"
            onClick={handleEditListing}
          >
            <AddEditIcon className='icon-spacing' />
            Edit Listing
          </Button>
        }

        <DialogBox
          isDialogOpen={editListingDialog}
          handleCloseDialog={closeDialog}
          title="Edit Listing"
        >
          <SellerListingForm
            handleCloseDialog={closeDialog}
            sellerId={sellerId}
            isEdit={editListingDialog}
            formValues={previousListingFormValues}
          />
        </DialogBox>
      </CardActions>
    </Card >
  );
}
