# MintFx: Currency Converter

## Presentation Script

### সময়

প্রায় ৫-৭ মিনিট

---

## Slide 1: পরিচিতি

**বলবেন:**

আসসালামু আলাইকুম। আমার project-এর নাম **MintFx**। এটি একটি simple currency converter Android application।

এই app-এর মাধ্যমে একজন user যেকোনো amount এক currency থেকে অন্য currency-তে convert করতে পারেন। যেমন, US Dollar থেকে Bangladeshi Taka, Indian Rupee বা Euro-তে conversion করা যায়।

---

## Slide 2: এই App কেন দরকার?

**বলবেন:**

আমাদের প্রতিদিনের জীবনে currency conversion-এর প্রয়োজন হয়। যেমন:

- বিদেশ ভ্রমণের সময়
- Online shopping করার সময়
- Freelancing বা international payment-এর ক্ষেত্রে
- বিদেশে পড়াশোনা বা কাজের খরচ হিসাব করার সময়
- অন্য দেশের product-এর price বোঝার জন্য

অনেক সময় calculator দিয়ে conversion করা যায়, কিন্তু exchange rate পরিবর্তনশীল। তাই একটি app দরকার যেটি latest rate ব্যবহার করে দ্রুত result দেখাবে।

---

## Slide 3: App-এর প্রধান Features

**বলবেন:**

MintFx-এর প্রধান features হলো:

- Amount input করা যায়
- Source currency নির্বাচন করা যায়
- Target currency নির্বাচন করা যায়
- BDT সহ অনেক currency support করে
- Currency option-এ code এবং country name দেখা যায়
- যেমন: `BDT-(Bangladesh)`
- Source এবং target currency swap করা যায়
- Latest exchange rate fetch করা যায়
- Internet না থাকলেও আগের saved rate দিয়ে কাজ করে
- App open হওয়ার সময় ছোট animation আছে
- Android app এবং GitHub Actions cloud build support করে

---

## Slide 4: App কীভাবে ব্যবহার করতে হয়?

**বলবেন:**

App ব্যবহার করা খুব সহজ।

প্রথমে user amount লিখবেন। এরপর `From` field থেকে যে currency আছে সেটি নির্বাচন করবেন। তারপর `To` field থেকে target currency নির্বাচন করবেন।

এরপর app automatically converted amount দেখাবে।

উদাহরণ:

```text
Amount: 100
From: USD-(United States)
To: BDT-(Bangladesh)
Result: প্রায় 11,950 BDT
```

Rate পরিবর্তন হলে user `Refresh rates` button চাপতে পারবেন।

---

## Slide 5: BDT এবং Country Name

**বলবেন:**

Currency বোঝা সহজ করার জন্য শুধু code দেখানো হয়নি। Code-এর সঙ্গে country বা region-এর নামও দেখানো হয়েছে।

উদাহরণ:

- `BDT-(Bangladesh)`
- `USD-(United States)`
- `INR-(India)`
- `EUR-(European Union)`
- `GBP-(United Kingdom)`

এতে beginner user-দের currency চিনতে সুবিধা হয়।

---

## Slide 6: Live Exchange Rate কোথা থেকে আসে?

**বলবেন:**

MintFx একটি public online service ব্যবহার করে latest exchange rate সংগ্রহ করে। এই service-এর নাম **ExchangeRate-API**।

App যে address-এ request পাঠায় তা হলো:

```text
https://open.er-api.com/v6/latest/USD
```

এখানে `USD` হলো base currency। Service আমাদের বিভিন্ন currency-এর rate পাঠায়।

যেমন response-এ থাকতে পারে:

```text
USD = 1.0
BDT = 119.5
EUR = 0.92
INR = 83.1
```

User-কে raw JSON দেখানো হয় না। App response বুঝে নিয়ে শুধু দরকারি result screen-এ দেখায়।

---

## Slide 7: Offline Feature

**বলবেন:**

এই app-এর সবচেয়ে গুরুত্বপূর্ণ এবং unique feature হলো **offline caching**।

সাধারণভাবে internet না থাকলে currency app কাজ নাও করতে পারে। কিন্তু MintFx একবার successfully rate পেলে সেটি phone-এর ভিতরে save করে রাখে।

পরের বার internet না থাকলেও app আগের saved rate ব্যবহার করে conversion চালাতে পারে।

যখন saved rate ব্যবহার হচ্ছে, screen-এ দেখা যায়:

```text
Offline mode - using saved rates
```

এতে user বুঝতে পারেন যে result live internet data নয়, আগের saved data ব্যবহার করছে।

---

## Slide 8: Offline Mode কীভাবে কাজ করে?

**বলবেন:**

App-এর data flow খুব সহজ:

```text
প্রথমে Internet থেকে rate আনার চেষ্টা
              ↓
       Rate পাওয়া গেলে
              ↓
     Phone-এ rate save করা
              ↓
        UI-তে result দেখানো
```

যদি internet request ব্যর্থ হয়:

```text
Internet request ব্যর্থ
              ↓
      Saved rate খোঁজা হয়
              ↓
     Saved rate থাকলে ব্যবহার
              ↓
      Offline badge দেখানো
```

তবে app যদি প্রথমবার চালু করা হয় এবং তখন internet না থাকে, তাহলে কোনো saved rate থাকবে না। সেই ক্ষেত্রে app error message দেখাবে।

---

## Slide 9: Phone-এ Data কীভাবে Save হয়?

**বলবেন:**

ছোট একটি rate list phone-এর ভিতরে save করার জন্য আমি Android-এর built-in **SharedPreferences** ব্যবহার করেছি।

এটি কোনো বড় database নয়। এটি ছোট key-value storage।

Rates-গুলো একটি JSON format-এ save হয়। উদাহরণ:

```json
{
  "USD": 1.0,
  "BDT": 119.5,
  "EUR": 0.92
}
```

এই project-এ Room database ব্যবহার করা হয়নি, কারণ আমাদের শুধু একটি ছোট rate list save করতে হয়। SharedPreferences এই কাজের জন্য সহজ এবং যথেষ্ট।

---

## Slide 10: Conversion কীভাবে হয়?

**বলবেন:**

App conversion করার জন্য একটি simple formula ব্যবহার করে:

```text
Converted amount =
Amount × Target rate ÷ Source rate
```

উদাহরণ:

```text
100 USD থেকে BDT
USD rate = 1.0
BDT rate = 119.5

100 × 119.5 ÷ 1.0 = 11,950 BDT
```

এই calculation-এর কাজ আলাদা একটি ছোট অংশে রাখা হয়েছে, যাতে formula সহজে বোঝা এবং পরিবর্তন করা যায়।

---

## Slide 11: App-এর ভিতরের সাধারণ Flow

**বলবেন:**

App-এর ভিতরে কাজের flow হলো:

```text
User
  ↓
Android Screen
  ↓
ViewModel
  ↓
Repository
  ↓
API অথবা Saved Cache
  ↓
ViewModel State
  ↓
Updated Screen
```

User সরাসরি API-এর সঙ্গে কথা বলেন না। Screen user-এর action ViewModel-কে জানায়। ViewModel Repository-কে data আনতে বলে। Repository API অথবা saved data থেকে result এনে ViewModel-কে দেয়। তারপর screen নতুন result দেখায়।

---

## Slide 12: Code সম্পর্কে যতটুকু জানা প্রয়োজন

**বলবেন:**

এই project-এ কয়েকটি file-এর আলাদা দায়িত্ব আছে:

- `MainActivity.kt`: App শুরু করে
- `CurrencyConverterScreen.kt`: App-এর screen এবং design
- `CurrencyViewModel.kt`: User action এবং screen state manage করে
- `CurrencyRepository.kt`: Internet অথবা saved data থেকে rate সংগ্রহ করে
- `CurrencyApiService.kt`: Online API call-এর নিয়ম নির্ধারণ করে
- `CurrencyPreferences.kt`: Saved rate phone-এ রাখা এবং পড়ার কাজ করে
- `CurrencyCalculator.kt`: Conversion formula চালায়
- `CurrencyUiState.kt`: Screen-এর current information রাখে

এভাবে প্রতিটি file-এর আলাদা দায়িত্ব থাকায় project বুঝতে এবং maintain করতে সুবিধা হয়।

---

## Slide 13: Technology Used

**বলবেন:**

এই app তৈরি করতে ব্যবহার করা হয়েছে:

- **Kotlin**: Android app-এর programming language
- **Jetpack Compose**: UI তৈরি করার modern toolkit
- **ViewModel**: UI state এবং user action manage করার জন্য
- **Retrofit**: Online API call করার জন্য
- **Gson**: JSON response পড়ার জন্য
- **SharedPreferences**: Offline rate save করার জন্য
- **Coroutines**: Background-এ network কাজ করার জন্য
- **GitHub Actions**: Cloud-এ build এবং validation করার জন্য

---

## Slide 14: GitHub Actions কেন ব্যবহার করেছি?

**বলবেন:**

এই project cloud-based build-এর জন্য তৈরি করা হয়েছে। তাই local computer-এ Android Studio বা Android SDK setup-এর উপর নির্ভর করা হয়নি।

GitHub Actions automatically:

1. Source code checkout করে
2. Java 17 setup করে
3. Gradle setup করে
4. Lint চালায়
5. Build চালায়
6. Debug APK তৈরি করে
7. APK artifact হিসেবে upload করে

তাই repository-তে code push করলেই cloud-এ build পরীক্ষা করা যায়।

---

## Slide 15: Demo করার সময় কী দেখাবেন?

**Demo flow:**

1. App open করুন
2. Startup animation দেখান
3. Amount হিসেবে `100` লিখুন
4. `USD-(United States)` select করুন
5. `BDT-(Bangladesh)` select করুন
6. Converted result দেখান
7. `SWAP CURRENCIES` button চাপুন
8. অন্য currency select করুন
9. `Refresh rates` চাপুন
10. Internet বন্ধ করে আবার refresh করুন
11. `Offline mode - using saved rates` badge দেখান

**বলবেন:**

এখানে app internet ছাড়া আগের saved data দিয়ে result দেখাচ্ছে। এটাই এই project-এর main unique feature।

---

## Slide 16: সীমাবদ্ধতা

**বলবেন:**

এই app-এর কিছু limitation আছে:

- Saved rate-এর সঙ্গে exact save time দেখানো হচ্ছে না
- প্রথমবার internet ছাড়া চালালে কোনো cached rate পাওয়া যাবে না
- API service unavailable হলে নতুন live rate পাওয়া যাবে না
- Exchange rate সবসময় পরিবর্তনশীল, তাই result financial advice হিসেবে ব্যবহার করা উচিত নয়

তবে সাধারণ conversion এবং offline demonstration-এর জন্য app যথেষ্ট কার্যকর।

---

## Slide 17: শেষ কথা

**বলবেন:**

MintFx একটি simple কিন্তু useful Android currency converter।

এই app live exchange rate ব্যবহার করে, কিন্তু network failure হলেও আগের saved rate দিয়ে কাজ চালিয়ে যেতে পারে।

Project structure beginner-friendly রাখা হয়েছে, যাতে প্রতিটি file-এর দায়িত্ব সহজে বোঝা যায়। একই সঙ্গে GitHub Actions ব্যবহার করে cloud build এবং APK generation করা হয়েছে।

ধন্যবাদ।

---

# সম্ভাব্য প্রশ্ন এবং সহজ উত্তর

## প্রশ্ন: API বন্ধ থাকলে কী হবে?

**উত্তর:**

App আগে saved rate ব্যবহার করার চেষ্টা করবে। Saved rate থাকলে offline result দেখাবে। Cache না থাকলে error message দেখাবে।

## প্রশ্ন: Room database কেন ব্যবহার করা হয়নি?

**উত্তর:**

এই app-এ শুধু একটি ছোট rate map save করতে হয়। তাই SharedPreferences সহজ এবং যথেষ্ট। Room সাধারণত বড় বা relational data-এর জন্য বেশি উপযোগী।

## প্রশ্ন: BDT কীভাবে যোগ করা হয়েছে?

**উত্তর:**

API থেকে BDT rate পাওয়া যায়। UI-তে code-এর সঙ্গে country name যোগ করে `BDT-(Bangladesh)` format দেখানো হয়েছে।

## প্রশ্ন: User input ভুল হলে কী হয়?

**উত্তর:**

App শুধু valid numeric input গ্রহণ করে। Text বা invalid character গ্রহণ করে না।

## প্রশ্ন: API call কি UI বন্ধ করে দেয়?

**উত্তর:**

না। API call coroutine-এর মাধ্যমে background-এ চলে। তাই UI responsive থাকে।

## প্রশ্ন: এই app-এর main unique feature কী?

**উত্তর:**

Offline caching। একবার live rate পাওয়া গেলে সেটি phone-এ save হয় এবং internet না থাকলেও সেই rate ব্যবহার করে conversion করা যায়।

## প্রশ্ন: App-এর data কি user-এর বাইরে কোথাও যায়?

**উত্তর:**

Exchange rate পাওয়ার জন্য API-তে request যায়। Amount এবং personal information API-তে পাঠানো হয় না। Rate data phone-এর SharedPreferences-এ cache করা হয়।
