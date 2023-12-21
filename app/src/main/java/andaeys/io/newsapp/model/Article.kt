package andaeys.io.newsapp.model

data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String="",
    val url: String?,
    val urlToImage: String?
)

fun dummyArticle(): Article = Article(
    author =  "Frank Schwab",
    title= "Eagles defense gives up a game-winning drive to Drew Lock's Seahawks - Yahoo Sports",
    description= "The Eagles' defense was good until it needed to be.",
    url= "https://sports.yahoo.com/eagles-defense-gives-up-a-game-winning-drive-to-drew-lock-seahawks-041934238.html",
    urlToImage= "https://s.yimg.com/ny/api/res/1.2/hJZZdK.VdFc7s7vAbBLvMA--/YXBwaWQ9aGlnaGxhbmRlcjt3PTEyMDA7aD04MDA-/https://s.yimg.com/os/creatr-uploaded-images/2023-12/bfb60620-9e25-11ee-bfff-5259d6d3eb17",
    publishedAt= "2023-12-19T14:11:12Z",
    content= "The Philadelphia Eagles did pretty much everything right on defense for the first 58 minutes of Monday night's game.",
    source = Source("", "CNN")
)
