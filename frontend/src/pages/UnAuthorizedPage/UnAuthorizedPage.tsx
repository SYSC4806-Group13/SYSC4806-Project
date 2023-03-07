import React from "react";
import PageHeader from "src/components/PageHeader/PageHeader";

const UnAuthorizedPage = () => {
  return (
    <PageHeader headerTitle="UnAuthorized">
      <div>
        <h2>This is a sample of an logged out page</h2>
        <p>If you see this, you are not loggedin</p>
      </div>
    </PageHeader>
  );
};

export default UnAuthorizedPage;
