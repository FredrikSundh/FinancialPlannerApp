This android app includes a budgeting tool to keep track of expenses as well as an investment return calculator.

Index returns for main american stock indices can be gotten and automatically calculated but for this you need to get your own alphavantage apikey which you need to declare as below in an ApiKey.kt file under a new folder called 
secret. Note that the returns calculated for these indices are the CAGR calculated using the price for the earliest alphavantage response as a startingpoint.

After this to run the app simply run it using android studio on their built in android emulators. Otherwise export the apk and install on another emulator or on your mobile device.

object ApiKey {
    const val API_KEY = "YOUR API KEY"
}

Disclaimer: This application was made solely for the interest in developing such an application, I take no responsibility for the accuracy of any data presented in the application
as apiresponses may change if alphavantage changes their api.
