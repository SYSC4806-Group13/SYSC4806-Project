import * as React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { Button, CardActionArea, CardActions } from '@mui/material';
import { capitalize } from '@mui/material/utils';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import 'src/styles/ListingCard.css'

export interface IListingCardProps {
    cardName: string,
    author: string,
    price: string,
    image: string,
    alt: string
}

export default function ListingCard (props: IListingCardProps) {
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
      </CardActions>
    </Card>
  );
}
