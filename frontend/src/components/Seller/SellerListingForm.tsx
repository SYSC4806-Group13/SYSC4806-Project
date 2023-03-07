import * as React from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { DialogActions, Button } from "@mui/material";
import CustomTextField from "src/components/Form/TextField";
import { useHttpClient } from "src/hooks/http-hook";
import { LISTING } from "src/constants/endpoints";

export interface ISellerListingFormProps {
  handleCloseDialog: () => void;
  sellerId: string | undefined;
}

interface IFormInput {
  isbn: string;
  title: string;
  author: string;
  publisher: string;
  description: string;
  inventory: number;
  price: number;
  releaseDate: string;
}

export default function SellerListingForm(props: ISellerListingFormProps) {
  const defaultValues = {
    sellerUserId: props.sellerId,
    isbn: "",
    title: "",
    author: "",
    publisher: "",
    description: "",
    inventory: 0,
    price: 0.0,
    releaseDate: new Date().toISOString().slice(0, 10),
  };
  const { sendRequest } = useHttpClient();
  const formMethods = useForm({ defaultValues });
  const {
    handleSubmit,
    control,
    formState: { errors },
  } = formMethods;
  const onSubmit: SubmitHandler<IFormInput> = async (data) => {
    const dataCopy = JSON.parse(JSON.stringify(data));
    dataCopy.sellerUserId = parseInt(dataCopy.sellerUserId);
    dataCopy.price = parseFloat(dataCopy.price).toFixed(2);
    dataCopy.inventory = parseInt(dataCopy.inventory);
    dataCopy.coverImage = "/static/images/book-cover.jpg";
    await sendRequest(LISTING, "POST", dataCopy);
    props.handleCloseDialog();
  };
  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <CustomTextField
        label="ISBN"
        name="isbn"
        number
        control={control}
        required
        errors={errors}
      />
      <CustomTextField
        label="Title"
        name="title"
        control={control}
        required
        errors={errors}
      />
      <CustomTextField
        label="Author"
        name="author"
        control={control}
        required
        errors={errors}
      />
      <CustomTextField
        label="Publisher"
        name="publisher"
        control={control}
        required
        errors={errors}
      />
      <CustomTextField
        label="Description"
        name="description"
        multiline
        control={control}
        required
        errors={errors}
      />
      <CustomTextField
        label="Inventory"
        name="inventory"
        number
        notFull
        minVal={0}
        control={control}
        required
        errors={errors}
      />
      <CustomTextField
        label="Price"
        name="price"
        number
        notFull
        startAdornment="$"
        control={control}
        required
        errors={errors}
      />
      <CustomTextField
        label="Release Date"
        name="releaseDate"
        date
        notFull
        control={control}
        required
        errors={errors}
      />
      <DialogActions>
        <Button onClick={props.handleCloseDialog} color="error">
          Cancel
        </Button>
        <Button color="success" type="submit">
          Submit
        </Button>
      </DialogActions>
    </form>
  );
}
