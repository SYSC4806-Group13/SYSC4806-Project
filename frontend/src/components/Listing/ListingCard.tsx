import * as React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { Button, CardActionArea, CardActions } from '@mui/material';
import { capitalize } from '@mui/material/utils';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import AddEditIcon from '@mui/icons-material/Edit';
import 'src/styles/ListingCard.css'
import {useParams} from "react-router-dom";
import AddIcon from "@mui/icons-material/Add";
import ListingGrid from "./ListingGrid";
import DialogBox from "../Dialog/DialogBox";
import SellerListingForm from "../Seller/SellerListingForm";
import IFormInput from "../Seller/SellerListingForm";

export interface IListingCardProps {
    cardName: string,
    title: string,
    author: string,
    price: string,
    image: string,
    alt: string,
    listingId : string,
    isbn: string,
    publisher: string,
    description: string,
    inventory: string,
    releaseDate: string,
}

export default function ListingCard (props: IListingCardProps) {
    const [editListingDialog, setEditListingDialog] = React.useState(false);

    const handleEditListing = () => {
        setEditListingDialog(true);

    };

    let { sellerId } = useParams();

    const closeDialog = () => setEditListingDialog(false);

    const iForm = {
        isbn: props.isbn,
        title: props.title,
        author: props.author,
        publisher: props.publisher,
        description: props.description,
        inventory: parseInt(props.inventory),
        price: parseFloat(props.price),
        releaseDate: props.releaseDate,
        listingId : props.listingId
    };

  return (
    <Card>
      <CardActionArea>
        <CardMedia
          component="img"
          image={props.image}
          height="300"
          alt={props.alt}
          className="image"
        />
        <CardContent>
          <Typography variant="h3">
            {capitalize(props.cardName)}
          </Typography>
          <Typography gutterBottom variant="h5" color="text.secondary">
            {props.author}
          </Typography>
          <Typography variant="h4" color="text.secondary">
            ${props.price}
          </Typography>
        </CardContent>
      </CardActionArea>
      <CardActions className='align-center'>
        <Button size="large" color="success">
          <Typography variant="h6">
            Add to Cart
          </Typography>
          <AddShoppingCartIcon className='icon-spacing'/>
        </Button>
          <Button
              variant="contained"
              color="primary"
              size="medium"
              className="button"
              onClick={handleEditListing}
          >
              <AddEditIcon className='icon-spacing'/>
              Edit Listing <AddIcon fontSize="large" />
          </Button>
        <DialogBox
            isDialogOpen={editListingDialog}
            handleCloseDialog={closeDialog}
            title = "Edit Listing"
        >
            <SellerListingForm
                handleCloseDialog={closeDialog}
                sellerId={sellerId}
                isEdit = {editListingDialog}
                formValues={iForm}
            />
        </DialogBox>
      </CardActions>
    </Card>
  );
}
