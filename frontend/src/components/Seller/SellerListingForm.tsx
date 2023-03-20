import * as React from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { DialogActions, Button, Typography } from "@mui/material";
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
    inventory: 1,
    price: 0.0,
    releaseDate: new Date().toISOString().slice(0, 10),
  };

  const [coverFile, setCoverFile] = React.useState<File>();
  const [isFileValidated, setIsFileValidated] = React.useState<boolean>(true);

  const { sendRequest } = useHttpClient();
  const formMethods = useForm({ defaultValues });
  const {
    handleSubmit,
    control,
    formState: { errors },
  } = formMethods;
  const onSubmit: SubmitHandler<IFormInput> = async (data) => {
    if (!!!isFileValidated) {
      return;
    }
    const dataCopy = JSON.parse(JSON.stringify(data));
    dataCopy.sellerUserId = parseInt(dataCopy.sellerUserId);
    dataCopy.price = parseFloat(dataCopy.price).toFixed(2);
    dataCopy.inventory = parseInt(dataCopy.inventory);
    const listing: any = await sendRequest(LISTING, "POST", dataCopy);
    const formData: any = new FormData();
    formData.append("imageFile", coverFile);
    await sendRequest("/covers/" + listing.listingId, "POST", formData);
    props.handleCloseDialog();
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const validContentTypes = ["image/jpg", "image/jpeg", "image/png"];

    if (!e.target.files) return;
    if (!validContentTypes.includes(e.target.files[0].type)) {
      setIsFileValidated(false);
      return;
    }
    setIsFileValidated(true);
    setCoverFile(e.target.files[0]);
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
        minVal={1}
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
      <Typography variant="h6"> Upload Cover Image </Typography>
      {!isFileValidated && (
        <Typography color={"red"}>
          {" "}
          Invalid File Type. Must be [png, jpg, jpeg]{" "}
        </Typography>
      )}
      <input
        required
        type="file"
        accept=".png, .jpg, .jpeg"
        onChange={handleFileChange}
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
