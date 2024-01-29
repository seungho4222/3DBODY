import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import MyPage from "../pages/mypage/MyPage";
import MyInfoPage from "../pages/mypage/MyInfoPage";
import NoticePage from "../pages/mypage/NoticePage";
import FAQPage from "../pages/mypage/FAQPage";
import DiaryPage from '../pages/diary/DiaryPage';

const router = createBrowserRouter([
  {
    path: "/",
    element:<App/>,
    children:[
      {
        path: "/mypage",
        Component: MyPage
      },
      {
        path: "/myinfo",
        Component: MyInfoPage
      },
      {
        path: "/notice",
        Component: NoticePage
      },
      {
        path: "/FAQ",
        Component: FAQPage
      },
      {
        path: "/diary",
        Component: DiaryPage
      }
    ]
  },
]);

export default router
