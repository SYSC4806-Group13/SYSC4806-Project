import * as React from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { DialogActions, Button } from "@mui/material";
import CustomTextField from "src/components/Form/TextField";
import { useHttpClient } from "src/hooks/http-hook";
import { LISTING } from "src/constants/endpoints";

export interface ISellerListingFormProps {
  handleCloseDialog: () => void;
  sellerId: string | undefined;

   isEdit : boolean;

   formValues? : IFormInput;
}


export interface IFormInput {
  isbn: string;
  title: string;
  author: string;
  publisher: string;
  description: string;
  inventory: number;
  price: number;
  releaseDate: string;
  listingId?: string;
}

export default function SellerListingForm(props: ISellerListingFormProps) {
  const defaultValues = {
    sellerUserId: props.sellerId,
    isbn:props.formValues && props.isEdit ?  props.formValues?.isbn : "",
    title:props.formValues && props.isEdit ? props.formValues?.title : "",
    author:props.formValues && props.isEdit ? props.formValues?.author : "",
    publisher:props.formValues && props.isEdit ? props.formValues?.publisher : "",
    description:props.formValues && props.isEdit ? props.formValues?.description : "",
    inventory:props.formValues && props.isEdit ? props.formValues?.inventory : 0,
    price:props.formValues && props.isEdit ? props.formValues?.price : 0.0,
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
      if(props.isEdit)
      {
          const dataCopy = JSON.parse(JSON.stringify(data));
          dataCopy.sellerUserId = parseInt(dataCopy.sellerUserId);
          dataCopy.price = parseFloat(dataCopy.price).toFixed(2);
          dataCopy.inventory = parseInt(dataCopy.inventory);
          dataCopy.coverImage = "/static/images/book-cover.jpg";
          if(props.formValues?.listingId)
            dataCopy.listingId = parseInt(props.formValues.listingId);

          await sendRequest(LISTING, "PUT", dataCopy);
          props.handleCloseDialog();
          window.location.reload();
      }
      else
      {
          const dataCopy = JSON.parse(JSON.stringify(data));
          dataCopy.sellerUserId = parseInt(dataCopy.sellerUserId);
          dataCopy.price = parseFloat(dataCopy.price).toFixed(2);
          dataCopy.inventory = parseInt(dataCopy.inventory);
          dataCopy.coverImage = "/static/images/book-cover.jpg";
          await sendRequest(LISTING, "POST", dataCopy);
          props.handleCloseDialog();
      }

  };
  //Another function that deal with patch, endpoint so be there, front end call to that
    // The form on sumbit, need to differenetiate between new and edit (isEdit? edit ; onSubmit)
    //Or do it in the function
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
