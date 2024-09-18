import { useState, useContext } from "react";

// react-router-dom components
import { Link } from "react-router-dom";

// @mui material components
import Card from "@mui/material/Card";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDInput from "components/MDInput";
import MDButton from "components/MDButton";

// Authentication layout components
import BasicLayout from "layouts/authentication/components/BasicLayout";

// Images
import bgImage from "assets/images/bg-sign-in-basic.jpeg";

// Authentication
import { CognitoUser, AuthenticationDetails } from "amazon-cognito-identity-js";
import UserPool from "UserPool";
import { AccountContext } from "Account";

function Basic() {
  const [rememberMe, setRememberMe] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const { authenticate } = useContext(AccountContext);

  const onSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage("");

    authenticate(username, password).then(data=> {
      // console.log("Logged in!", data);
    })
    .catch(err => {
      console.error("Failed to login", err);
      setErrorMessage("Failed to login. Invalid username or password.");
    })

    // const authDetails = new AuthenticationDetails({
    //   Username: username,
    //   Password: password,
    //   AuthParameters: {
    //     USERNAME: username,
    //     PASSWORD: password
    //   },
    // });


    // const poolData = new CognitoUser({
    //   Username: username,
    //   Pool: UserPool
    // });


    // try {
    //   await new Promise((resolve, reject) => {
    //     poolData.authenticateUser(authDetails, {
    //       onSuccess: (data) => {
    //         console.log("onSuccess: ", data);
    //         resolve(data);
    //         history.push("/dashboard");
    //       },
    //       onFailure: (err) => {
    //         console.log(authDetails)
    //         console.log("onFailure: ", err);
    //         reject(err);
    //       },
    //       newPasswordRequired: (data) => {
    //         console.log("newPasswordRequired: ", data);
    //         resolve(data);
    //       }
    //     });
    //   });
    // } catch (error) {
    //   console.error("Authentication error:", error);
    // }
  };


  return (
    <BasicLayout image={bgImage}>
      <Card>
        <MDBox
          variant="gradient"
          bgColor="info"
          borderRadius="lg"
          coloredShadow="info"
          mx={2}
          mt={-3}
          p={2}
          mb={1}
          textAlign="center"
        >
          <MDTypography variant="h4" fontWeight="medium" color="white" mt={1} mb={1}>
            Sign in
          </MDTypography>
        </MDBox>
        <MDBox pt={4} pb={3} px={3}>
          <MDBox component="form" role="form" onSubmit={onSubmit}>
            {errorMessage && (
              <MDBox mb={2}>
                <MDTypography variant="caption" color="error" textAlign="center">
                  {errorMessage}
                </MDTypography>
              </MDBox>
            )}
            <MDBox mb={2}>
              <MDInput type="username" label="Username" fullWidth value={username} onChange={(e) => setUsername(e.target.value)} />
            </MDBox>
            <MDBox mb={2}>
              <MDInput type="password" label="Password" fullWidth value={password} onChange={(e) => setPassword(e.target.value)} />
            </MDBox>
            <MDBox mt={4} mb={1}>
              <MDButton type="submit" variant="gradient" color="info" fullWidth>
                Sign In
              </MDButton>
            </MDBox>
            <MDBox mt={3} mb={1} textAlign="center">
              <MDTypography variant="button" color="text">
                Don&apos;t have an account?{" "}
                <MDTypography
                  component={Link}
                  to="/authentication/sign-up"
                  variant="button"
                  color="info"
                  fontWeight="medium"
                  textGradient
                >
                  Sign up
                </MDTypography>
              </MDTypography>
            </MDBox>
          </MDBox>
        </MDBox>
      </Card>
    </BasicLayout>
  );
}


export default Basic;
