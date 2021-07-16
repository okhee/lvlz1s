# lvlz1s Documentation

This site contains the API documentation for lvlz1s(https://github.com/okhee/lvlz1s), 
a RESTful API that supports quiz solving, uploading audio files, and creating quiz sets and quizzes.

## Table of Content
- [Entity](#entity)
  - [QuizSet](#quizset)
  - [Quiz](#quiz)
  - [Question](#question)
  - [AudioFile](#audiofile)
  - [User](#user)
  
- [API Endpoints](#api-endpoints)
  - [QuizSet API](#quizset-api)
    - [Get all quiz sets](#get-all-quiz-sets)
    - [Create new quiz set](#create-new-quiz-set)
    - [Get specific quiz set](#get-specific-quiz-set)
  - [Quiz API](#quiz-api)
    - [Start new quiz instance](#start-new-quiz-instance)
    - [Get ( finished / ongoing ) quiz information](#get--finished--ongoing--quiz-information)
    - [Get question information of quiz](#get-question-information-of-quiz)
    - [Submit an answer to the question of the quiz](#submit-an-answer-to-the-question-of-the-quiz)
    - [Request an additional hint for the question of the quiz](#request-an-additional-hint-for-the-question-of-the-quiz)
    - [Finish and submit quiz instance](#finish-and-submit-quiz-instance)
    - [Give up ongoing quiz instance](#give-up-ongoing-quiz-instance)
  - [AudioFile API](#audiofile-api)
    - [Upload a audio file](#upload-a-audio-file)
    - [Upload multiple audio files](#upload-multiple-audio-files)
    - [Get audio file stream](#get-audio-file-stream)
  - [User API](#user-api)
    - [Sign up a new user](#sign-up-a-new-user)
    - [Get currently logged-in user information](#get-currently-logged-in-user-information)
  - [Login API](#login-api)
    - [Sign in and receive JWT token](#sign-in-and-receive-jwt-token)
  
## Entity

## QuizSet


## Quiz

Instance of running, finished quiz (exam, test).

### `Quiz` Properties

- `id` - Long
- `quizSet` -  QuizSet

    - The associated quiz set

- `owner` - User
- `songList` - List<Question>

    - The list of `Questions` (order matters)

    - `Questions` are randomly chosen from songs of associated quiz set.

- `responseMap` - Map<Long, Long>

  >{0L:55L, 3L:55L, 10L:55L}
  >
    - The map of song id response that user submitted

    - key: questionId (0 ~ `questionNum - 1`)

- `scoreList` - Map<Long, Boolean>

    - The map of whether submitted response is correct or wrong.

    - key: questionId (0 ~ `questionNum - 1`)

- `questionNum` - Long
- `closed` - Boolean
    - Whether quiz has ended.

### Create `Quiz` instance

> `POST` `/quiz`

Check authority to given `QuizSet`.

Create a new quiz instance if equivalent previous unfinished `Quiz` does not exists.

- equivalent : `userId` and `quizSetId` is identical
- unfinished : `closed` = `false`

Randomly choose `songNum` `Questions` from `songPool` of `QuizSet`.

If previous `Quiz` exists, return such `Quiz`.

Response header "Location" specifies corresponding URL, `/quiz/{qid}`.

#### parameters

| Name      | Type    | In   | Description                                                  |
|-----------|---------|------|--------------------------------------------------------------|
| userId    | integer | body |                                                              |
| quizSetId | integer | body | `user` must be allowed to given `QuizSet`                        |
| songNum   | integer | body | Number of `Question` that you want to include in quiz instance |

### Get current status of `Quiz` (before submission)

> `GET` `/quiz/{quizId}`

### Get `Question`

> `GET` `/quiz/{quizId}/q/{questionId}`

Get `Question` of `quizId` and `questionId`

#### parameters
| Name           | Type    | In   | Description                                                |
|----------------|---------|------|------------------------------------------------------------|
| quizId         | integer | path |                                                            |
| q (questionId) | integer | path | Specify index of `Question`. Default: `1` (one-based indexing) |

### Submit `Question` response

> `POST` `/quiz/{quizId}/q/{questionId}`

Save response to `responseMap` of `Quiz` instance

If `questionId` is out of bounds, redirect to submit page

If not, proceed to next `Question`

#### parameters
| Name           | Type    | In   | Description                                                |
|----------------|---------|------|----|
| quizId         | integer | path |   |
| questionId     | integer | path |   |

### Submit `Quiz`

> `POST` `/quiz/{quizId}`

Finish and submit quiz

### Check result of `Quiz`

> `GET` `/quiz/{quizId}/result`

### Give up ongoing `Quiz`

> `DELETE`  `/quiz/{quizId}`

## Question

## AudioFile

## User

<hr>

## API Endpoints

## QuizSet API
### Get all quiz sets


### Create new quiz set

### Get specific quiz set


## Quiz API
### Start new quiz instance

### Get ( finished / ongoing ) quiz information

### Get question information of quiz

### Submit an answer to the question of the quiz

### Request an additional hint for the question of the quiz

### Finish and submit quiz instance

### Give up ongoing quiz instance


## AudioFile API
### Upload a audio file
### Upload multiple audio files
### Get audio file stream
## User API
### Sign up a new user
### Get currently logged-in user information
## Login API
### Sign in and receive JWT token