"use client";

import PageBreadcrumb from "@/components/common/PageBreadCrumb";
import DynamicTable, { ColumnConfig } from "@/components/tables/DynamicTable";
import Pagination from "@/components/tables/Pagination";
import React from "react";

export default function Apikeys() {
  const handlePageChange = (page: number) => {
    console.log("Page changed to:", page);
  };

  const handleEdit = (row: any) => console.log("Edit:", row);
  const handleDelete = (row: any) => console.log("Delete:", row);

  const columns = [
    { key: "user", header: "User", type: "profile" },
    { key: "status", header: "Status", type: "status" },
    { key: "budget", header: "Budget", type: "text" },
    { key: "edit", header: "Edit", type: "edit" },
    { key: "delete", header: "Delete", type: "delete" },
  ] as const satisfies ColumnConfig[];


  const data = [
    {
      user: {
        image: "/images/user/user-17.jpg",
        name: "Lindsey Curtis",
        email: "hossein@gmail.com",
      },
      status: "Active",
      budget: "3.9K",
    },
    {
      user: {
        image: "/images/user/user-20.jpg",
        name: "Abram Schleifer",
        email: "reza@iden.com",
      },
      status: "Cancel",
      budget: "2.8K",
    },
  ];


  return (
    <>
      <PageBreadcrumb pageTitle="API Keys" />
      <DynamicTable columns={columns} data={data} onEdit={handleEdit} onDelete={handleDelete} />
      <div className="mt-5">
        <Pagination
          currentPage={5}
          totalPages={10}
          onPageChange={handlePageChange}
        />
      </div>

    </>
  );
}