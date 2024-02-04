import { createBrowserRouter } from "react-router-dom";

import App from "../App";
import MyPage from "../pages/mypage/MyPage";
import MyInfoPage from "../pages/mypage/MyInfoPage";
import NoticePage from "../pages/mypage/NoticePage";
import FAQPage from "../pages/mypage/FAQPage";
import DiaryPage from "../pages/diary/DiaryPage";
import ChallengePage from "../pages/challenge/ChallengePage";
import ChallengeDetailPage from "./../pages/challenge/ChallengeDetailPage";
import DiaryTrainingPage from "../pages/diary/DiaryTrainingPage";
import DiaryFoodPage from "../pages/diary/DiaryFoodPage";
import HomePage from "../pages/home/HomePage";
import PasswordChangePage from "../pages/mypage/PasswordChangePage";
import TrainingLoadPage from "../pages/diary/training/TrainingLoadPage";
import TrainingChoicePage from "./../pages/diary/training/TrainingChoicePage";
import MyRoutinePage from "./../pages/diary/training/MyRoutinePage";
import RoutineCreatePage from "./../pages/diary/training/RoutineCreatePage";
import RoutineLoadPage from "./../pages/diary/training/routine/RoutineLoadPage";
import RoutineChoicePage from "./../pages/diary/training/routine/RoutineChoicePage";
import RoutineEditPage from "./../pages/diary/training/RoutineEditPage";
import ChallengeRegistrationPage from "./../pages/challenge/ChallengeRegistrationPage";
import ChallengeUpdatePage from "./../pages/challenge/ChallengeUpdatePage";
import WebRTCPage from "./../pages/challenge/WebRTCPage";
import PinChangePage from "../pages/mypage/PinChangePage";
import FoodCategoryPage from "../pages/diary/food/FoodCategoryPage";
import FoodAddFage from "../pages/diary/food/FoodAddFage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      {
        path: "",
        Component: HomePage,
      },
      {
        path: "/diary",
        Component: DiaryPage,
      },
      {
        path: "/diary/training/:year/:month/:date",
        Component: DiaryTrainingPage,
      },
      {
        path: "/diary/food/:year/:month/:date",
        Component: DiaryFoodPage,
      },
      {
        path: "/diary/training/load",
        Component: TrainingLoadPage,
      },
      {
        path: "/diary/training/choice",
        Component: TrainingChoicePage,
      },
      {
        path: "/diary/training/myroutine",
        Component: MyRoutinePage,
      },
      {
        path: "/diary/training/myroutine/create",
        Component: RoutineCreatePage,
      },
      {
        path: "/diary/training/myroutine/load",
        Component: RoutineLoadPage,
      },
      {
        path: "/diary/training/myroutine/choice",
        Component: RoutineChoicePage,
      },
      {
        path: "/diary/training/myroutine/edit",
        Component: RoutineEditPage,
      },
      {
        path: "/diary/food/:category",
        Component: FoodCategoryPage,
      },
      {
        path: "/diary/food/:category/add",
        Component: FoodAddFage,
      },
      {
        path: "/mypage",
        Component: MyPage,
      },
      {
        path: "/mypage/myinfo",
        Component: MyInfoPage,
      },
      {
        path: "/mypage/passwordchange",
        Component: PasswordChangePage,
      },
      {
        path: "/mypage/pinchange",
        Component: PinChangePage,
      },
      {
        path: "/mypage/notice",
        Component: NoticePage,
      },
      {
        path: "/mypage/FAQ",
        Component: FAQPage,
      },
      {
        path: "/challenge",
        Component: ChallengePage,
      },
      {
        path: "challenge/:challengeId",
        Component: ChallengeDetailPage,
      },
      {
        path: "challenge/registration",
        Component: ChallengeRegistrationPage,
      },
      {
        path: "challenge/:challengeId/update",
        Component: ChallengeUpdatePage,
      },
      {
        path: "/challenge/ongoing",
        Component: WebRTCPage,
      },
    ],
  },
]);

export default router;
