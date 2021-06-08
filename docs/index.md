# lvlz1s Documentation

This site contains the API documentation for lvlz1s(https://github.com/okhee/lvlz1s), 
a RESTful API that supports quiz solving, uploading audio file and creating quiz sets and quizs.

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

> `POST` `/quizs`

Check authority to given `QuizSet`.

Create a new quiz instance if equivalent previous unfinished `Quiz` does not exists.

- equivalent : `userId` and `quizSetId` is identical
- unfinished : `closed` = `false`

Randomly choose `songNum` `Questions` from `songPool` of `QuizSet`.

If previous `Quiz` exists, return such `Quiz`.

Response header "Location" specifies corresponding URL, `/quizs/{qid}`.

#### parameters

| Name      | Type    | In   | Description                                                  |
|-----------|---------|------|--------------------------------------------------------------|
| userId    | integer | body |                                                              |
| quizSetId | integer | body | `user` must be allowed to given `QuizSet`                        |
| songNum   | integer | body | Number of `Question` that you want to include in quiz instance |

### Get current status of `Quiz` (before submission)

> `GET` `/quizs/{quizId}`

### Get `Question`

> `GET` `/quizs/{quizId}/q/{questionId}`

Get `Question` of `quizId` and `questionId`

#### parameters
| Name           | Type    | In   | Description                                                |
|----------------|---------|------|------------------------------------------------------------|
| quizId         | integer | path |                                                            |
| q (questionId) | integer | path | Specify index of `Question`. Default: `1` (one-based indexing) |

### Submit `Question` response

> `POST` `/quizs/{quizId}/q/{questionId}`

Save response to `responseMap` of `Quiz` instance

If `questionId` is out of bounds, redirect to submit page

If not, proceed to next `Question`

#### parameters
| Name           | Type    | In   | Description                                                |
|----------------|---------|------|----|
| quizId         | integer | path |   |
| questionId     | integer | path |   |

### Submit `Quiz`

> `POST` `/quizs/{quizId}`

Finish and submit quiz

### Check result of `Quiz`

> `GET` `/quizs/{quizId}/result`

### Give up ongoing `Quiz`

> `DELETE`  `/quizs/{quizId}`

## QuizSet

## Question

## AudioFile
