# CoffeeApp
This project is compatible with Android 4.+ and built with Android Studio.

To build please have the latest SDK version installed on your computer, clone or download from Github, open in Android Studio as an existing project and run the application.

I wanted to make this project very friendly to Android 5.0. Though the project is runnable on 4.0 devices, I encourage you to build and run the sample on a device running 5.+ to get the full experience of this project. Since the story board seemed to be a generic, I added a lot of my own personality and taste into the project in between each frame while still meeting the expectations of how each frame was supposed to look. I originally intended to use a cardview but chose not to as I felt it would make the design look a tad further from how the story board intends it to look. 

I thought heavily about how I wanted the project to be built and in the end I ruled the project too small to implement any significant software design patterns. You will find many Singleton methods inside the Utils class, however. You can view how I implement other design patterns in my other projects here on Github. The project is very "face value" - meaning it is designed for your needs and requirements. If this project were to be integrated into a larger application, I would have added many other necessary features such as a refresh button, but I intentionally did not add due to the nature of the project and it's static dataset. I also chose to design the application to implement both asynchronous loading and pre loading. Pre 5.0 devices will take adavantage of the asynchronous loading while 5.0+ devices will perform pre loading of the data before it is viewed. I personally prefer an asynchronous approach to make the application more user-friendly as far as cost is concerned. Though both have their advantages and disadvantages.

I would like to note that I met all the requirements and "Nice to Haves". I decided to use the Volley library to make api calls over Retrofit and Robospice because Volley is the native library Google provides and I tend to stick close to Google when it comes to software development (Team Nexus =D)! I also chose Volley because I knew I was going to use Sergey Tarasevich's' Universal ImageLoader, which provides many "under the hood" configurations to make both asynchronous and basic image loading easy and efficient. On top of this, the cancellation management and request priority is something I have not seen matched in any library similar. Though this is a little heavier than Picasso, in my opinion because of its capabilities, is a better choice. Had I used Retrofit or Robospice I would have paired it with the peer library Picasso. 


Lastly the "back button" at the top left is not the exact back button as the story board has, yet it is the back button already included for all Android projects. I believe this is the only view that does not look exactly like the story board.

Please contact me with any questions or concerns about the project.

Joshua Williams
