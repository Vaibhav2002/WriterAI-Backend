![](graphics/0.png)

# **WriterAI** - A Revolutionary AI Based Content Writing Tool
## Use the Power of Artificial Intelligence to write high quality content

<br/>

**WriterAI** is an AI based content writing tool that helps users easily write high quality emails, blogs, letters, thesis and other stuff

This project is developed by **Team 404** for Hashnode x PlanetScale hackathon. This team comprises of [Ishant](https://github.com/ishantchauhan710/) and [Vaibhav](https://github.com/Vaibhav2002).

## Blog :writing_hand:
To know more about how we built this project, read my article here: <br>
[https://ishantchauhan.hashnode.dev/WriterAI-securely-store-access-and-share-your-files-on-cloud](https://ishantchauhan.hashnode.dev/WriterAI-securely-store-access-and-share-your-files-on-cloud)

## About

![](graphics/pojects.png)

**WriterAI** is an AI based content writing tool that helps users easily write high quality emails, blogs, letters, thesis and other stuff. One can share their project with others and work as a team.
[Here](https://youtu.be/m9J6BRdM-yo) is a short video on how to use WriterAI

## Live Usage

You can experience the power of WriterAI from [here](https://writerai.netlify.app)

## WriterAI Features

![](graphics/2.png)
![](graphics/3.png)
![](graphics/4.png)
![](graphics/5.png)

- **Auto Text Completion** - Generates up to 120 words with an input of just 5 words
- **Inbuilt Text Editor** - For writing your blogs, emails, letters, e-books and other stuff
- **Project Sharing** - You can share your project with unlimited number of users and work in teams
- **Markdown Support** - You can add images, tables, hyperlinks and code snippets in your projects by using markdown syntax
- **Project Download** - You can download your projects in JSON, XML and Markdown formats
- **Platform Compatibility** - WriterAI is highly responsive and has been tested on the latest versions of Chrome, Safari and Opera Mini
- **User Authentication** - Supports Email-Password authentication and Google Mail authentication
- **User Authorization** - WriterAI keeps on validating the user's auth tokens every 5 minutes automatically in order to prevent any unauthorized access

## Technical details

- The frontend part is written using ReactJS.

- The backend of WriterAI is built using the power of Ktor, Kotlin and PlanetScale! It follows MVC architecture, DRY and SOLID code principles. Currently, the backend part is deployed on Linode, which is a popular cloud service provider platform for deploying your backend and database!


### Setup [Backend]
To set up the backend of this project, you will need a cloud hosting service provider like Linode, Digital Ocean or Amazon Web Services. Once you get the account, you need to configure a server where your backend will run on. For this project, I have used an ubuntu server however, you can go with any server you are comfortable with. Once you are done with this, follow the following steps to setup the backend of this project!

1. Install the following tools on your server: nodejs, npm, git, ufw, mongodb

2. Configure the object storage provided by your hosting provider.

3. Open the http and https port on your server using ufw

4. Clone this repository and navigate to the root folder

5. Create a file named ".env" and there, write:

```
MONGO_URL=mongodb://127.0.0.1:27017/WriterAI
PORT=XXXX
JWT_SECRET=XXXXXXXX
STORAGE_ACCESS_KEY=XXXXXXXX
STORAGE_SECRET_KEY=XXXXXXXX
STORAGE_BUCKET=XXXXXXXX
STORAGE_REGION=XXXXXXXX
STORAGE_URL=XXXXXXXX
ENCRYPTION_KEY=XXXXXXXX
SIGNATURE_KEY=XXXXXXXX
```

- Here, MONGO_URL is the url where your mongodb service will run on. Since we will be running it on the server itself and our nodejs server will communicate using it, we have assigned the localhost url to the MONGO_URL variable

- Assign any value to the PORT. It is the port on which your nodejs server will run on

- JWT_SECRET is the secret variable that will be used to encrypt the user passwords. You can assign it with any value you want

- STORAGE_ACCESS_KEY, STORAGE_SECRET_KEY, STORAGE_BUCKET, STORAGE_REGION and STORAGE_URL are the variables that refer to the object storage of your hosting. An object storage is the place where the actual files of the users will get stored. You can easily get their values by refering to your hosting provider's manual

- ENCRYPTION_KEY will be used to encrypt the files of the user. You can use a base 64 generator and assign it with any 32 bytes base64 value

- SIGNATURE_KEY will be used to sign the encryption key. In encryption, a signature is a proof that the user has the private key which matches with some public key. You can use a base 64 generator and assign it with any 64 bytes base64 value

6. Now, you need to somehow enable the your linux server to listen to the http/https requests and transfer them to your nodejs server's port. You can use a reverse proxy or any tool like nginx or apache to do so. However, for testing, you can simply open your nodejs port publically using ufw and make requests to that port!

7. Install all the required packages to run the nodejs backend by writing "npm install" command in the root folder

8. Finally start the nodejs backend server using "npm start" command


![Linode](graphics/linode.png)

### Built With [Backend] 🛠
* [Linode](https://linode.com) - Linode is a popular cloud hosting service provider where you can deploy your backend
* [Ktor](https://ktor.io) - Create asynchronous client and server applications using the power of Kotlin.
* [Kotlin](https://kotlinlang.org) - A modern programming language that makes developers happier.
* [PlanetScale](https://planetscale.com) - A MySQL-compatible serverless database platform.
* [Jetbrains Exposed](https://github.com/JetBrains/Exposed) - Jetbrains Exposed is an ORM framework for Kotlin.
* [Koin](https://insert-koin.io) - Koin is a smart Kotlin dependency injection library.
* [Firebase](https://firebase.google.com) - Uses Firebase Admin SDK for authenticating and validate user's JWT auth token.

### Architecture [Backend]
The backend of WriterAI uses [***MVC (Model View Controller)***](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller) architecture.

![](https://miro.medium.com/max/1018/1*Bls-krmgBxDrULOoBwRNUA.png)

#### This is how the API flow works
![Api Flow](graphics/api_flow.jpg)

### Package Structure [Backend] :open_file_folder:

![Package Structure](graphics/package_structure.jpg)

    main                            # Root Directory
    .
    ├── kotlin.com.writerai         # Contains all the Ktor server code
    |   ├── data                    # Contains all the data handling code, which includes Database, DataSources, Repositories, Data models and Response models
    |   ├── controllers             # Contains files with business logic of specified routes
    │   ├── di                      # Contains module which configuration of dependency inject in the project
    │   ├── plugins                 # Contains all the required plugins to be used by Ktor Server like authentication, logging, routes and many more
    │   ├── routes                  # Contains all the API routes like all routes for Projects, user and more
    │   └── util                    # Utility files to make work easier like extension functions and helper classes
    |
    └── resources                   # Contains all the configurations required to run the Ktor Server


### Database Schemas 
#### User Schema
```kotlin
object UserTable : IdTable<String>() {
    val email: Column<String> = varchar("email", 500).uniqueIndex()
    val username: Column<String> = varchar("username", 100)
    val totalApiReqMade: Column<Int> = integer("totalApiReqMade")
    override val id: Column<EntityID<String>> = varchar("id", 100).entityId()
    override val primaryKey = PrimaryKey(id)
}
```
#### Project Schema
```kotlin
object ProjectTable : IntIdTable() {
    val userId: Column<String> = varchar("userId", 100)
    val title: Column<String> = varchar("title", 5000)
    val description: Column<String> = varchar("description", 5000)
    val content: Column<String> = text("content")
    val coverPic:Column<String> = varchar("coverPic", 1000)
    val timeStamp: Column<Long> = long("timeStamp")
}
```
#### ShareTo Schema
```kotlin
object ShareTable : IntIdTable() {
    val ownerId: Column<String> = varchar("ownerId", 100)
    val sharedTo: Column<String> = varchar("sharedTo", 100)
    val sharedToEmail:Column<String> = varchar("sharedToEmail", 500)
    val projectId: Column<Int> = integer("projectId")
}
```

### Available APIs [Backend] :computer:
The backend of WriterAI provides many APIs to perform different operations such as insert user, getProjects, share Project, get all shared projects etc. All the available APIs and their corresponding routes can be found inside the routes folder.

![](graphics/postman.jpg)

## Attribution
The project uses [PlanetScale](https://planetscale.com) as its database and is made for the [Hashnode X PlanetScale Hackathon](https://townhall.hashnode.com/planetscale-hackathon).

## Contact
For any queries, you can [connect with me](https://vaibhavjaiswal.vercel.app/#/)
  



