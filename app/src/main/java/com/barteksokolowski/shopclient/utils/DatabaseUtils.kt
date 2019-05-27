package com.barteksokolowski.shopclient.utils

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import com.barteksokolowski.shopclient.R

import com.barteksokolowski.shopclient.data.BookContract.BookEntry
import com.barteksokolowski.shopclient.data.BookDbHelper
import com.barteksokolowski.shopclient.model.Book
import com.barteksokolowski.shopclient.ui.activities.RecommendationActivity

object DatabaseUtils {

    fun getCartValue(context: Context): Double = BookDbHelper(context).getCartValue()

    fun getListOfBooksInCart(context: Context): ArrayList<Book> = BookDbHelper(context).getCartList()

    fun checkIfDatabaseIsEmpty(context: Context) {
        val dbHelper = BookDbHelper(context)
        if (dbHelper.getNumberOfBooks() == 0) {
            fillDatabase(context)
        }
    }

    fun isRecommendationAvailable(context: Context) {
        val dbHelper = BookDbHelper(context)
        if (dbHelper.getNumberOfOrderedBooks() > 10) {
            showRecommendationDialog(context)
        }
    }

    private fun showRecommendationDialog(context: Context) = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.recommendation_found))
            .setMessage(context.getString(R.string.recommendation_found_message))
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.yes), { dialog, _ ->
                dialog.dismiss()
                context.startActivity(Intent(context, RecommendationActivity::class.java))
            })
            .setNegativeButton(context.getString(R.string.no), { dialog, _ ->
                dialog.dismiss()
            })
            .create()
            .show()

    fun getRecommendationList(context: Context) = BookDbHelper(context).getRecommendations()

    private fun insertBookToDatabase(context: Context, book: Book) {
        val values = ContentValues()
        values.put(BookEntry.COLUMN_TITLE, book.title)
        values.put(BookEntry.COLUMN_AUTHOR, book.authors)
        values.put(BookEntry.COLUMN_CATEGORY, book.category)
        values.put(BookEntry.COLUMN_PRICE, book.price)
        values.put(BookEntry.COLUMN_PHOTO_URL, book.photoURL)
        values.put(BookEntry.COLUMN_NOTE, book.note)

        context.contentResolver.insert(BookEntry.CONTENT_URI, values)
    }

    private fun fillDatabase(context: Context) {
        val bookArray = arrayOf(
                Book(1, "Lalka", "Bolesław Prus", 0, "http://s.lubimyczytac.pl/upload/books/4416000/4416737/564419-352x500.jpg",
                        29.99, "Stanisław Wokulski zakochuje się w zobaczonej w teatrze arystokratce Izabeli Łęckiej i całe swoje życie i wszystkie marzenia podporządkowuje zdobyciu panny Izabeli: rezygnuje z pasji naukowej , gromadzi majątek, pomaga jej rodzinie finansowo, stara się wejść do wyższych sfer, spełnia jej kaprysy... gdyż wydaje mu się, że wszystko to przybliża go do niej..."),
                Book(2, "Zbrodnia i kara", "Fiodor Dostojewski", 0, "http://s.lubimyczytac.pl/upload/books/4052000/4052126/603306-352x500.jpg",
                        36.49, "\"Zbrodnia i kara\" to powieść zaliczana do arcydzieł literatury światowej. Opowiada historię zbrodni, której dokonał były ubogi student Rodion Raskolnikow. Co skłoniło go do tego kroku? Czym można to wytłumaczyć? Czy istnieje cokolwiek, co pozwala usprawiedliwić zabójstwo? Co dzieje się z umysłem mordercy? Jaką karę poniósł Raskolnikow?"),
                Book(3, "Syzyfowe prace", "Stefan Żeromski", 0, "http://s.lubimyczytac.pl/upload/books/69000/69831/352x500.jpg",
                        14.99, "Skupiając się na historii jednego z uczniów gimnazjum, Żeromski wprowadza nas w realia ówczesnej szkoły i stosunków pomiędzy nauczycielami i ich podopiecznymi, pozwala nam zrozumieć, na czym polegał proces rusyfikacji, obnaża pogardliwy stosunek Rosjan do Polaków, mówi wreszcie o trudzie walki, jaką część polskiej młodzieży podejmowała z wynarodowieniem."),
                Book(4, "Mistrz i Małgorzata", "Michaił Bułhakow", 0, "http://s.lubimyczytac.pl/upload/books/3873000/3873903/519438-352x500.jpg",
                        23.36, "Mistrz i Małgorzata wymyka się jednoznacznemu opisowi, prowokując mnogość interpretacji. Znaleźć w niej można rozważania na temat kondycji sztuki, echa nieustannego konfliktu twórcy z otoczeniem, klasyczny filozoficzny motyw walki dobra ze złem, ale również ciętą satyrę oraz ironiczne aluzje do radzieckiej rzeczywistości lat trzydziestych."),
                Book(5, "Pan Tadeusz", "Adam Mickiewicz", 0, "https://siedmiorog.pl/media/catalog/product/cache/1/thumbnail/9df78eab33525d08d6e5fb8d27136e95/p/a/pan_tadeusz_okladka-przod_1.jpg",
                        25.88, "Pan Tadeusz, czyli ostatni zajazd na Litwie jest polską epopeją narodową należącą do kanonu literatury polskiej i światowej. Akcja epopei rozgrywa się w czasach wojen napoleońskich w kręgach polskiego ziemiaństwa na terenach Litwy."),
                Book(6, "Folwark zwierzęcy", "George Orwell", 0, "http://s.lubimyczytac.pl/upload/books/48000/48379/352x500.jpg",
                        11.89, "Orwell pisał: „»Folwark zwierzęcy« miał być przede wszystkim satyrą na rewolucję rosyjską. Mój morał brzmi tak oto: rewolucje mogą przynieść radykalną poprawę, gdy masy będą czujne i będą wiedzieć, jak pozbyć się swych przywódców, gdy tamci zrobią, co do nich należy.\"."),
                Book(7, "Jądro ciemności", "Joseph Conrad", 0, "http://s.lubimyczytac.pl/upload/books/262000/262117/402591-352x500.jpg",
                        16.47, "Autobiograficzna powieść Josepha Conrada o jego podróży do Kongo, która doczekała się dwóch adaptacji filmowych. Prosta z pozoru fabuła staje się tłem do ukazania prawdziwego oblicza kolonializmu przełomu XIX i XX wiek, w którym nie chodziło o niesienie przysłowiowego kaganka oświaty ludom Afryki, lecz o ich wyzysk i zniewolenie."),
                Book(8, "Przedwiośnie", "Stefan Żeromski", 0, "http://s.lubimyczytac.pl/upload/books/296000/296343/459047-352x500.jpg",
                        9.87, "Polska w dwudziestoleciu międzywojennym: trudna droga powrotna Polaków ze Wschodu do ojczyzny, zamęt i chaos w Warszawie 1920 roku, `Cud nad Wisłą`, blaski i cienie życia na polskiej prowincji w kraju, który po stu latach niewoli odzyskał wreszcie upragnioną wolność."),
                Book(9, "Ludzie bezdomni", "Stefan Żeromski", 0, "http://s.lubimyczytac.pl/upload/books/223000/223153/291782-352x500.jpg",
                        8.02, "Doktor Tomasz Judym po zakończonej praktyce lekarskiej w Paryżu powraca do Warszawy. Ze smutkiem stwierdza, że nie ma szans na poprawienie losu ludzi z nizin społecznych, ludzi, z których i on pochodzi. Ani posada lekarza w ekskluzywnym uzdrowisku w Cisach, ani miłość do Joasi Podborskiej nie są w stanie zagłuszyć jego poczucia winy wobec najuboższych."),
                Book(10, "Granica", "Zofia Nałkowska", 0, "http://s.lubimyczytac.pl/upload/books/144000/144512/352x500.jpg",
                        16.49, "On i ona – oboje z dobrego domu, żyjący w niewielkim miasteczku, obracający się w wytwornych towarzystwach, na salonach, w pięknych strojach. Polowania, herbatki, konwencjonalne spotkania, przy których kurtuazyjne konwersacje. Romans z chłopką jako niewygodna acz przecież powszechna podszewka tego małomiasteczkowego życia. Na pozór nic się nie dzieje."),
                Book(11, "Hobbit", "J.R.R. Tolkien", 1, "http://s.lubimyczytac.pl/upload/books/4821000/4821058/628377-352x500.jpg",
                        19.44, "Bilbo Baggins to hobbit, który lubi wygodne, pozbawione niespodzianek życie, rzadko podróżując dalej niż do swojej piwnicy. Jego błogi spokój zostaje jednak zakłócony, gdy pewnego na jego progu pojawia się czarodziej Gandalf z gromadą krasnoludów, by porwać go na prawdziwą przygodę. Wszyscy wyruszają po wielkie skarby strzeżone przez Smauga Wspaniałego."),
                Book(12, "Bractwo Pierścienia", "J.R.R. Tolkien", 1, "http://s.lubimyczytac.pl/upload/books/261000/261556/401115-352x500.jpg",
                        12.42, "W spokojnej wiosce w Shire młody hobbit Frodo zostaje obarczony niezwykle odpowiedzialnym zadaniem. Ma on podjąć niebezpieczną podróż przez Śródziemie do Szczeliny Zagłady, by tam zniszczyć Pierścień Jedyny. Musi bowiem udaremnić niecne plany Władcy Ciemności..."),
                Book(13, "Dwie wieże", "J.R.R. Tolkien", 1, "http://s.lubimyczytac.pl/upload/books/265000/265271/410944-352x500.jpg",
                        12.42, "Po śmierci Boromira i pojmaniu Merry'ego i Pippina rozbite Bractwo Pierścienia podąża niestrudzenie ku swemu przeznaczeniu, choć żaden z jego członków nie wie, co dzieje się z Samem i Frodem. A tropiony przez Nazgule Powiernik Pierścienia z uporem idzie dalej. Podążając za Gollumem, przemierza Martwe Bagna, by stanąć wreszcie u stóp Minas Morgul."),
                Book(14, "Powrót Króla", "J.R.R. Tolkien", 1, "http://s.lubimyczytac.pl/upload/books/265000/265275/410948-352x500.jpg",
                        12.42, "Zebrały się armie Władcy Ciemności, a jego przerażający cień sięga coraz dalej. Połączone siły ludzi, krasnoludów, elfów i entów stają naprzeciw nawałnicy mroku. Tymczasem Frodo i Sam uparcie wędrują w głąb Mordoru, zamierzając zrealizować cel ich heroicznej misji i zniszczyć Pierścień Jedyny."),
                Book(15, "Gra o tron", "George R.R. Martin", 1, "http://s.lubimyczytac.pl/upload/books/3937000/3937616/523293-352x500.jpg",
                        32.02, "W Zachodnich Krainach o ośmiu tysiącach lat zapisanej historii widmo wojen i katastrofy nieustannie wisi nad ludźmi. Zbliża się zima, lodowate wichry wieją z północy, gdzie schroniły się wyparte przez ludzi pradawne rasy i starzy bogowie. Minęły już lata pokoju i oto możnowładcy zaczynają grę o tron."),
                Book(16, "Krew elfów", "Andrzej Sapkowski", 1, "http://s.lubimyczytac.pl/upload/books/240000/240512/490973-352x500.jpg",
                        31.72, "Geralt wraz z Ciri osiadają w wiedźmińskiej warowni. Tam Dziecko - Niespodzianka uczy się jak walczyć, jak radzić sobie z mieczem i przeciwnikiem, ale Ciri ma też inne umiejętności, które przerażają nawet Vesemira. Przy jej magicznych zdolnościach pomoc czarodziejki okazuje się niezbędna."),
                Book(17, "Ostatnie życzenie", "Andrzej Sapkowski", 1, "http://s.lubimyczytac.pl/upload/books/240000/240310/490965-352x500.jpg",
                        31.72, "Wiedźmin to mistrz miecza i fachowiec czarostwa strzegący moralnej i biologicznej równowagi w cudownym świecie fantasy. Z woli Sapkowskiego w ów świat pełen potworów i bujnych charakterów, skomplikowanych intryg i eksplodujących namiętności wnosi Geralt nasze problemy, mitologie i nowoczesny punkt widzenia."),
                Book(18, "Miecz przeznaczenia", "Andrzej Sapkowski", 1, "http://s.lubimyczytac.pl/upload/books/240000/240312/490966-352x500.jpg",
                        31.72, "Wiedźmin Geralt przyłącza się jednak do zorganizowanej przez króla Niedamira wyprawy na smoka, który skrył się w jaskiniach Gór Pustulskich."),
                Book(19, "Krew elfów", "Andrzej Sapkowski", 1, "http://s.lubimyczytac.pl/upload/books/240000/240512/490973-352x500.jpg",
                        31.72, "Drżyjcie, albowiem nadchodzi Niszczyciel Narodów. Stratują waszą ziemię i sznurem ją podzielą. Miasta wasze zostaną zburzone i pozbawione mieszkańców. Nietoperz i kruk w domach waszych zamieszkają, drzewo straci liść, zgnije owoc i zgorzknieje ziarno. Zaprawdę powiadam wam, oto nadchodzi czas miecza i topora, wiek wilczej zamieci."),
                Book(20, "Sezon burz", "Andrzej Sapkowski", 1, "http://s.lubimyczytac.pl/upload/books/199000/199630/490986-352x500.jpg",
                        29.24, "Sezon Burz to w wiedźmińskiej historii rzecz osobna, nie prapoczątek i nie kontynuacja. Jak pisze autor: Opowieść trwa. Historia nie kończy się nigdy…"),
                Book(21, "Dziady", "Adam Mickiewicz", 2, "http://s.lubimyczytac.pl/upload/books/50000/50618/352x500.jpg",
                        6.98, "Dziady Adama Mickiewicza to powstały na przełomie kilkunastu lat dramat romantyczny, który stanowi bodaj najlepszy przykład tego gatunku literackiego i przez wielu badaczy twórczości wieszcza zwany jest \"arcydramatem\"."),
                Book(22, "Makbet", "William Shakespeare", 2, "http://s.lubimyczytac.pl/upload/books/256000/256388/387183-352x500.jpg",
                        9.74, "Macbeth i Banquo, generałowie armii królewskiej, wracając do domów po zwycięskiej bitwie, spotykają na swojej drodze trzy czarownice. Przepowiadają one pierwszemu z nich rychłe wyniesienie na króla, drugiemu pozostawiając jednak przywilej poczęcia królewskiego rodu."),
                Book(23, "Romeo i Julia", "William Shakespeare", 2, "http://s.lubimyczytac.pl/upload/books/3963000/3963790/527118-352x500.jpg",
                        7.71, "Cała Werona boleje nad konfliktem między rodami Kapuletich i Montekich - najpotężniejszych rodzin w mieście. Tymczasem młody Romeo Monteki przez przypadek udaje się na bal do domu wroga i poznaje tam piękną Julię Kapuleti. Okazuje się, że miłość pokonuje rodzinną nienawiść."),
                Book(24, "Wesele", "Stanisław Wyspiański", 2, "http://s.lubimyczytac.pl/upload/books/70000/70018/352x500.jpg",
                        6.44, "Koniec XIX wieku - w galicyjskim zaciszu Polacy powoli przywykają do tego, że ich kraju nie ma na mapach Europy. Nie wszyscy się z tym pogodzili, każdy z uczestników bronowickiego wesela nosi w sobie marzenie o niepodległości Polski."),
                Book(25, "Antygona", "Sofokles", 2, "http://s.lubimyczytac.pl/upload/books/139000/139929/352x500.jpg",
                        8.78, "Heroiczna bohaterka Sofoklesa przekonuje o istnieniu odwiecznych, niepisanych, tkwiących w nas praw moralnych, boskich z pochodzenia i niewzruszalnych, którym sprzeniewierzyć się nie wolno, nawet gdy ceną za dochowanie im wierności staje się własne życie."),
                Book(26, "Król Edyp", "Sofokles", 2, "http://s.lubimyczytac.pl/upload/books/179000/179724/147094-352x500.jpg",
                        8.9, "Król Edyp Sofoklesa (V wiek p.n.e.), jeden z największych dramatów wszech czasów, przez Arystotelesa uznany za „najpiękniejszą tragedię”, mimo dwóch i pół tysiąca lat, jakie upłynęły od jego powstania, wciąż jest utworem żywym i fascynującym, stale powracającym na scenę."),
                Book(27, "Skąpiec", "Molier", 2, "http://s.lubimyczytac.pl/upload/books/48000/48889/352x500.jpg",
                        7.79, "Harpagon, tytułowy skąpiec, ma dwoje dzieci, Kleant zakochany jest w Mariannie, tak samo jak jego ojciec, a Eliza w Walerym. Muszą jednak ukrywać swą miłość ze względu na Harpagona."),
                Book(28, "Hamlet", "William Shakespeare", 2, "http://s.lubimyczytac.pl/upload/books/213000/213727/250156-352x500.jpg",
                        11.82, "„Hamlet” należy do najwybitniejszych sztuk Shakespeare`a. Przedstawia on historię duńskiego księcia, który postanawia pomścić skrytobójczo zamordowanego ojca. Zadanie to tym trudniejsze, że zabójcą jest aktualnie panujący król."),
                Book(29, "Balladyna", "Juliusz Słowacki", 2, "http://s.lubimyczytac.pl/upload/books/80000/80022/352x500.jpg",
                        7.77, "Jeden z najbardziej znanych dramatów Juliusza Słowackiego - opowieść o dwóch siostrach, Alinie i Balladynie, które, by rozstrzygnąć spór o rękę księcia Kirkora postanawiają nazbierać w lesie malin."),
                Book(30, "Tango", "Sławomir Mrożek", 2, "http://s.lubimyczytac.pl/upload/books/234000/234034/652076-352x500.jpg",
                        14.6, "\"Tango\" to dramat o współczesnym społeczeństwie, w którym panuje konformizm, anarchia, brak postaw. Farsową sytuację kompletnego pomieszania i rozprzężenia opanowuje i wykorzystuje władza, czyli silny i brutalny cham."),
                Book(31, "Co widać i czego nie widać", "Frédéric Bastiat", 3, "http://s.lubimyczytac.pl/upload/books/0/652/352x500.jpg",
                        9.75, "Co widać i czego nie widać to krótka opowieść o tym, co najważniejsze w ekonomii. Okazuje się, że nie trzeba czytać opasłych podręczników, aby zrozumieć istotę procesu gospodarowania i jego konsekwencji. To jedna z tych książek, które czyta się przez całe życie i za każdym razem odkrywa się w niej coś nowego, inspirującego."),
                Book(32, "Prawo", "Frédéric Bastiat", 3, "http://s.lubimyczytac.pl/upload/books/60000/60927/352x500.jpg",
                        9.99, "Czym jest prawo? Czym powinno być? Jaki jest jego zakres działania? Jakie są jego granic? Gdzie kończą się kompetencje prawodawcy? Co powinno być celem prawa?\nOdpowiedzi na te fundamentalne pytania udziela, w swoim stylu, wielki francuski pamflecista i pisarz, Frédric Bastiat."),
                Book(33, "Państwo", "Frédéric Bastiat", 3, "http://s.lubimyczytac.pl/upload/books/68000/68106/352x500.jpg",
                        12.00, "Bastiat stawia niezwykle ważną kwestię, od rozstrzygnięcia której zależą wybory ideologiczne. Słusznie krytykując i mnogość i nieuniknioną w tej sytuacji sprzeczność oczekiwań wobec państwa, wzywa do zastanowienia, czy państwo, ze względu na to, czym jest naprawdę, jest w ogóle zdolne do ich spełnienia."),
                Book(34, "Wielki szort", "Michael Lewis", 3, "http://s.lubimyczytac.pl/upload/books/289000/289007/441312-352x500.jpg",
                        24.05, "Wielki Szort. Mechanizm maszyny zagłady to pierwszy dokument, który szczegółowo przedstawia kulisy największego ogólnoświatowego kryzysu naszych czasów. Lewis opowiada historię równie wciągającą i niezwykłą jak w swoich poprzednich bestsellerach, dowodząc po raz kolejny, że jest znakomitym, obdarzonym humorem kronikarzem naszych czasów."),
                Book(35, "Planowany chaos", "Ludwig von Mises", 3, "http://s.lubimyczytac.pl/upload/books/149000/149548/352x500.jpg",
                        27.99, "Charakterystyczną cechą naszej epoki dyktatorów, wojen i rewolucji jest jej antykapitalistyczne nastawienie. Większość rządów i partii politycznych gorąco pragnie ograniczyć sferę inicjatywy prywatnej i wolnej przedsiębiorczości..."),
                Book(36, "Ludzkie działanie - traktat o ekonomii", "Ludwig von Mises", 3, "http://s.lubimyczytac.pl/upload/books/0/557/352x500.jpg",
                        68.82, "Opus magnum nestora austriackiej szkoły ekonomii Ludwiga von Misesa. Chłodna, rzeczowa rozprawa z socjalizmem, interwencjonizmem i wszelkimi odmianami pseudoekonomii."),
                Book(37, "Złoto, banki, ludzie - krótka historia pieniądza", "Murray Newton Rothbard", 3, "http://s.lubimyczytac.pl/upload/books/97000/97702/352x500.jpg",
                        26.99, "Profesor Rothbard dowodzi, że rząd zawsze i wszędzie pozostawał wrogiem mocnego pieniądza. Rząd i uprzywilejowane grupy interesu za pomocą karteli bankowych i inflacji grabią dochody ludzi, osłabiają wartość pieniądza na rynku oraz powodują recesje i depresje."),
                Book(38, "Ekonomia i polityka", "Ludwig von Mises", 3, "http://s.lubimyczytac.pl/upload/books/96000/96196/352x500.jpg",
                        21.99, "Ekonomia i polityka jest bardzo przystępnym wprowadzeniem do podstawowych zasad ekonomicznych. W tych sześciu wykładach, Profesor Mises dyskutuje wiele z najważniejszych problemów ekonomicznej teorii i polityki, między innymi: zysku i straty, pieniądza i kredytu, kapitału i inwestycji, teorii cen."),
                Book(39, "Ekonomia w jednej lekcji", "Henry Hazlitt", 3, "http://s.lubimyczytac.pl/upload/books/94000/94492/352x500.jpg",
                        32.84, "Książka Hazlitta pozwala czytelnikowi zrozumieć podstawowe zagadnienia ekonomiczne, a zwłaszcza wyrobić sobie pogląd na tematy powszechnie dziś dyskutowane: na czym powinna polegać rola rządu w gospodarce, jak chronić przemysł, jakie są skutki ceł, regulacji cen, robót publicznych, jaka jest rola związków zawodowych itd."),
                Book(40, "Czysty kod. Podręcznik dobrego programisty", "Robert Cecil Martin", 4, "http://s.lubimyczytac.pl/upload/books/83000/83492/352x500.jpg",
                        60.99, "O tym, ile problemów sprawia niedbale napisany kod, wie każdy programista. Nie wszyscy jednak wiedzą, jak napisać ten świetny, \"czysty\" kod i czym właściwie powinien się on charakteryzować. Co więcej - jak odróżnić dobry kod od złego? Odpowiedź na te pytania oraz sposoby tworzenia czystego, czytelnego kodu znajdziesz właśnie w tej książce."),
                Book(41, "Symfonia C ++ Standard", "Jerzy Grębosz", 4, "http://s.lubimyczytac.pl/upload/books/48000/48858/352x500.jpg",
                        105.00, "Jeśli szukasz nowoczesnego języka programowania, to jest właśnie język C++. Jeśli chcesz nauczyć się tego języka w łatwy, pogodny sposób - książka „Symfonia C++ standard” jest właśnie dla Ciebie."),
                Book(42, "Sztuka podstępu. Łamałem ludzi, nie hasła", "Kevin Mitnick, William L. Simon", 4, "http://s.lubimyczytac.pl/upload/books/48000/48929/352x500.jpg",
                        43.99, "Sensacyjne historie opisane w książce pomogą w obronie przed najpoważniejszym zagrożeniem bezpieczeństwa - ludzką naturą."),
                Book(43, "Mistrz czystego kodu. Kodeks postępowania profesjonalnych programistów", "Robert Cecil Martin", 4, "http://s.lubimyczytac.pl/upload/books/207000/207899/232089-352x500.jpg",
                        37.99, "W kolejnych rozdziałach Robert C. Martin zapozna Cię z różnymi sposobami podejścia do testowania kodu oraz współpracy między programistami a innymi ludźmi. Książka ta jest długo wyczekiwaną pozycją na rynku - nie pozwól, żeby ktoś miał ją przed Tobą!"),
                Book(44, "Thinking in Java", "Bruce Eckel", 4, "http://s.lubimyczytac.pl/upload/books/51000/51620/352x500.jpg",
                        145.99, "Książka \"Thinking in Java\" zawiera szczegółowe omówienie zasad programowania w Javie. Przeznaczona jest dla początkujących programistów i dla ekspertów. Przystępnie prezentuje zarówno zagadnienia podstawowe, jak i zaawansowane. Dziesiątki przykładów ułatwiają zrozumienie każdego tematu. Wszystko to sprawia, że poznajemy prawdziwą Javę."),
                Book(45, "Wprowadzenie do algorytmów", "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein", 4, "http://s.lubimyczytac.pl/upload/books/146000/146862/352x500.jpg",
                        126.99, "Zostały w niej omówione metody matematyczne stosowane do analizy algorytmów, sortowanie i statystyki pozycyjne, struktury danych, podstawowe metody projektowania efektywnych algorytmów. Dużo miejsca poświęcono złożonym strukturom danych i podstawowym algorytmom grafowym."),
                Book(46, "Android w praktyce", "Charlie Collins, Michael Galpin, Matthias Kaeppler", 4, "http://s.lubimyczytac.pl/upload/books/186000/186398/164206-352x500.jpg",
                        99.00, "W trakcie lektury zobaczysz, jak tworzyć precyzyjne elementy graficzne, zarządzać zadaniami wykonywanymi w tle oraz równoległymi wątkami. Ponadto sprawdzisz, jak współużytkować dane między aplikacjami oraz komunikować się z usługami sieciowymi. To tylko niektóre z tematów poruszonych w tej wyjątkowej książce, poświęconej platformie Android."),
                Book(47, "Android Studio. Podstawy tworzenia aplikacji", "Andrzej Stasiewicz", 4, "http://s.lubimyczytac.pl/upload/books/287000/287049/459825-352x500.jpg",
                        29.49, "Jeśli chcesz nauczyć się programować telefony i tablety, zacznij naukę od przeczytania tej książki. Książka poprowadzi Cię przez trudny proces pisania pierwszych aplikacji w nowym, oficjalnym środowisku programistycznym Android Studio."),
                Book(48, "Programowanie aplikacji dla Androida. The Big Nerd Ranch Guide.", "Chris Stewart, Phillips Bill, Marsicano Kristin", 4, "http://s.lubimyczytac.pl/upload/books/4826000/4826484/635031-352x500.jpg",
                        96.99, "Jeśli umiesz pisać zorientowany obiektowo kod w Javie i postanowiłeś zacząć tworzyć aplikacje dla Androida, wziąłeś do ręki odpowiednią książkę. Jest to praktyczny, przystępnie napisany przewodnik, który bezboleśnie przeprowadzi Cię przez trudności, jakie napotyka właściwie każdy początkujący programista aplikacji dla Androida."),
                Book(49, "Android. Programowanie aplikacji. Rusz głową!", "David Griffiths, Dawn Griffiths", 4, "http://s.lubimyczytac.pl/upload/books/4152000/4152389/538900-352x500.jpg",
                        79.20, "Książka, którą trzymasz w rękach, to podręcznik niezwykły, gdyż uwzględnia specyfikę funkcjonowania ludzkiego mózgu i sposób, w jaki najszybciej się uczy. Dzięki nowatorskiemu podejściu autorów nauka pisania aplikacji nie jest nudna: niepostrzeżenie będziesz nabierał coraz większej wprawy."),
                Book(50, "Head First Java. Edycja polska (Rusz głową!)", "Kathy Sierra, Bert Bates", 4, "http://s.lubimyczytac.pl/upload/books/4824000/4824681/633193-352x500.jpg",
                        89.00, "Otwórz swój umysł. Poznaj język Java w niekonwencjonalny sposób. Nie będziesz musiał przebijać się przez długie wywody i czytać kilometrowych przykładów. Autorzy książki \"Head First Java. Edycja polska\" wybrali inny sposób przedstawienia czytelnikom najpopularniejszego języka programowania ery internetu.")
        )
        bookArray.forEach { insertBookToDatabase(context, it) }
    }
}