/* eslint-disable react/prop-types */
/* eslint-disable react/function-component-definition */

// @mui material components
import Icon from "@mui/material/Icon";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDButton from "components/MDButton";
import { Link } from "react-router-dom";
import React, { useEffect, useState } from 'react';
import useAxiosPrivate from 'hooks/AxiosPrivate'

export default function LogTable() {
  const [logs, setLogs] = useState([]);

  const Actions = ({ logId }) => (
    <MDBox display="flex" alignItems="center">
      <Link to={`/log-details/${logId}`}>
        <MDButton variant="outlined" color="info" size="small" style={{ marginLeft: "8px" }}>
          <Icon>search</Icon>
          &nbsp;Details
        </MDButton>
      </Link>
    </MDBox>
  );

  const axiosPrivate = useAxiosPrivate(); // Use your custom hook here

  useEffect(() => {
    const fetchLogs = async () => {
      try {
        // Use the axios instance from your custom hook
        const response = await axiosPrivate.get('/log');
        setLogs(response.data); // Assuming the API returns an array of log objects
      } catch (error) {
        console.error('Error fetching logs:', error);
      }
    };

    fetchLogs();
  }, [axiosPrivate]);


  // Maps fetched logs to table rows
  const rows = logs.map(log => ({
    logId: (
      <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
        {log.logId}
      </MDTypography>
    ),
    timestamp: (
      <MDTypography variant="caption" color="text" fontWeight="medium">
        {new Date(log.timeOfAction).toLocaleString()}
      </MDTypography>
    ),
    actionTaken: (
      <MDTypography variant="caption" color="text" fontWeight="medium">
        {log.actionTaken}
      </MDTypography>
    ),
    adminId: (
      <MDTypography variant="caption" color="text" fontWeight="medium">
        {log.adminId}
      </MDTypography>
    ),
    details: (
      <MDTypography variant="caption" color="text" fontWeight="medium">
        {log.details}
      </MDTypography>
    ),
    userAgent: (
      <MDTypography variant="caption" color="text" fontWeight="medium">
        {log.userAgent}
      </MDTypography>
    ),
    approximateLocation: (
      <MDTypography variant="caption" color="text" fontWeight="medium">
        {log.approximateLocation}
      </MDTypography>
    ),
    actions: <Actions logId={log.logId} />,
  }));


  return { columns: [
    { Header: "Log ID", accessor: "logId", align: "left", width: "15%" },
    { Header: "Timestamp", accessor: "timestamp", align: "left", width: "15%" },
    { Header: "Action", accessor: "actionTaken", align: "left", width: "15%" },
    // { Header: "Admin ID", accessor: "adminId", align: "left", width: "15%" },
    // { Header: "Details", accessor: "details", align: "left", width: "15%" },
    { Header: "User Agent", accessor: "userAgent", align: "left", width: "25%" },
    // { Header: "Approx. Location", accessor: "approximateLocation", align: "left", width: "10%" },
    { Header: "Actions", accessor: "actions", align: "center", width: "15%" },
  ],
  rows,
  }
}
