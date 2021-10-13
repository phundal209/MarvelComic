#README

###Overview

This project builds a simple UI for a Marvel Comic (deadpool). The UI shows the following data

*Title
*Description
*Thumbnail Image

###UI Architecture

The presentation layer of this project can be found in app/ui and follows a traditional MVVM pattern.

This is a single activity application, in which the activity nagivates to a fragment
(https://github.com/phundal209/MarvelComic/blob/master/Marvel/app/src/main/java/com/app/marvel/ui/ComicFragment.kt)
which is responsible for the binding layer of this project.

In order to achieve separation and cleaner testing, a separate layer is pulled from this fragment
that binds the data received to the binding (via view binding)
This view is ComicView (https://github.com/phundal209/MarvelComic/blob/master/Marvel/app/src/main/java/com/app/marvel/ui/ComicView.kt)

The view model injects a repository to emit view states back into this view, and takes the action
of fetching this comic data.

In order for the thumbnail to be rendered, I included Glide as a dependency.

###Network Layer

The networking layer of this project uses a combination of retrofit + okhttp + moshi
for serialization of objects, network requests, and building an http client. 

The network speaks to the endpoint for comic data (/v1/public/comics/) and returns a network response model

The Service can be found (https://github.com/phundal209/MarvelComic/blob/master/Marvel/app/src/main/java/com/app/marvel/network/ComicService.kt)

###Data Layer

The domain object that map the network model to the UI layer is called Comic. This model is implemented
in both the networking layer as well as the caching layer as the source of truth.

This layer is where the ComicRepository (https://github.com/phundal209/MarvelComic/blob/master/Marvel/app/src/main/java/com/app/marvel/data/ComicRepository.kt)
injects the service as well as a DAO for the cache. 

If the comic already exists in cache, another network request is not necessary and the model is returned

If the comic does not exist in cache, a network request is made and the model is then returned.

For caching, I brought in Room as a dependency.

###Hashing

According to the documentation (https://developer.marvel.com/documentation/authorization) all calls
made require a hashing strategy to make successful api calls.

HashGenerator is the class that generates a valid hash. I wrote the Private Key / Public Key as constants
in a .properties file and added it to the .gitignore so that I do not save it anywhere and is generated
at build time.

The hashing strategy is md5(timestamp + private key + public key) converted to hex.

NOTE: It is throttled to only make a certain number of requests, and due to unknown reasons some variations
of the hash do not work. It is not yet clear what causes differences. I was able to generate a valid
hash, but also an invalid hash using the same technique. 


###Testing

I wrote tests for the Repository as this is the glue between the network and UI. The tests here 
are basic unit tests that cover

1. Successful happy path of no cache + making network request
2. Successful happy path of cache + no network request needed
3. Failure + no save to cache
4. Failure + proper error message returned

For the UI test, I wrote a basic test container to launch my ComicView in. Since this view
is decoupled from the android lifecycle, any container that was able to inflate the binding
was sufficient. 

Using a TestComicFragment I was able to launch the ComicView and call a ViewState. The view state
was captured using Espresso view matchers. 

###Other libraries 

1. Hilt -> Dependency Injection
2. Lifecycle Libs -> view model
3. FragmentKtx
4. OkHttpLogging -> for debugging network responses