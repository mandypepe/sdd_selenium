(TC-018)User Story: Implement "Last" page navigation and validation
Description:
As a user navigating through a paginated list of results, I want to be able to click the "Last" button to jump directly to the final page of data without having to press the "Next" button repeatedly. This will improve efficiency and the navigation experience in the UI.

Acceptance Criteria:

    Correct Navigation: Given the user is on the initial page or any intermediate page, when they click the "Last" button, the application must load and display the records corresponding to the last page of results.

    No Errors: When the last page of results is loaded, the graphical interface must not display any error messages, and the browser console must be free of exceptions.

    Next Button State: Given the user is viewing the last page of results, the "Next" button must not appear in the interface, or alternatively, it must be visible but in a disabled state.

Derived Tasks:

    Title: Task: Analyze pagination requirements for "Last" button (TC-018)

        Description: Analyze the current pagination component and define the state structure needed to calculate the total number of pages.

        Acceptance Criteria: Brief technical documentation on the required variables (totalPages, totalRecords, pageSize) added to the ticket comments.

    Title: Task: Update UI component to include "Last" button

        Description: Add the visual "Last" button to the frontend pagination component.

        Acceptance Criteria: The "Last" button is visible, follows design standards, and is keyboard accessible.

    Title: Task: Implement logic to calculate total pages

        Description: Develop the function or hook that receives the total item count and page size, returning the exact number of the last page.

        Acceptance Criteria: Mathematical logic is correct (e.g., Math.ceil(totalRecords / pageSize)) and updates reactively if the total changes.

    Title: Task: Bind "Last" button click event to fetch final page

        Description: Bind the onClick event of the "Last" button to trigger the request or update state to the last page.

        Acceptance Criteria: Upon clicking the button, the component requests the correct page data from the backend or state manager.

    Title: Task: Implement logic to disable "Next" button on last page

        Description: Add conditional logic evaluating if the current page equals total pages to hide or disable the "Next" button.

        Acceptance Criteria: The "Next" button has the disabled=true attribute or CSS class when currentPage === totalPages.

    Title: Task: Handle loading state for last page transition

        Description: Ensure a loading indicator (spinner or skeleton) is shown while fetching the last page data.

        Acceptance Criteria: The loading indicator is visible during the transition and disappears when the last page data is rendered.

    Title: Task: Add error handling for edge cases (zero results)

        Description: Handle scenarios where there are no results (0 pages) or only a single page.

        Acceptance Criteria: If there are 0 or 1 total pages, the "Last" button should be hidden or disabled to prevent unnecessary requests.

    Title: Task: Write unit tests for "Last" navigation logic

        Description: Create unit tests (e.g., Jest, Vitest) for the page calculation function and conditional rendering.

        Acceptance Criteria: Test coverage over 80% on the newly implemented methods.

    Title: Task: Write UI automation tests for TC-018 scenarios

        Description: Automate the TC-018 test case using Cypress or Selenium.

        Acceptance Criteria: The automation script executes the flow of clicking "Last", validates data load, and verifies "Next" is disabled.

    Title: Task: Perform cross-browser manual testing

        Description: Perform manual testing of the pagination component across major browsers (Chrome, Edge, Firefox).

        Acceptance Criteria: "Last" navigation works correctly with no console errors across all supported browsers.

    Title: Task: Create PR and address code review feedback

        Description: Raise changes in a Pull Request, request a review, and resolve comments.