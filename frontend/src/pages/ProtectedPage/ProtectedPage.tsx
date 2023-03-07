import React from "react";
import PageHeader from "src/components/PageHeader/PageHeader";

const ProtectedPage = () => {
  return (
    <PageHeader headerTitle="Authorized">
      <div>
        <h2>This is a sample of an Authorized route</h2>
        <p>If you see this, you are loggedin</p>
      </div>
    </PageHeader>
  );
};

export default ProtectedPage;
