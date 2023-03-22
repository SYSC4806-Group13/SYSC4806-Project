import * as React from "react";
import ListingGrid from "src/components/Listing/ListingGrid";
import PageHeader from "src/components/PageHeader/PageHeader";
import { LISTING } from "src/constants/endpoints";
import { useHttpClient } from "src/hooks/http-hook";
import { buildListings } from "src/utils/listings";

export interface IAllListingsProps { }

export default function AllListings(props: IAllListingsProps) {
  const [listings, setListings] = React.useState([]);
  const { sendRequest } = useHttpClient();

  // Retrieving listings
  React.useEffect(() => {
    const getSellerItems = async () => {
      let items = await sendRequest(LISTING, "GET", {});
      items = buildListings(items);
      setListings(items);
    };
    getSellerItems();
  }, [sendRequest]);

  return (
    <React.Fragment>
      <PageHeader headerTitle="Listing">
        <ListingGrid listings={listings} />
      </PageHeader>
    </React.Fragment>
  );
}
