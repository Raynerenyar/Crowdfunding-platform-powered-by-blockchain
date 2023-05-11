//SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract Crowdfunding is Ownable {
    address public admin;
    address public tokenAddress;
    address public crowdfundingFactory;

    IERC20 public token;
    string public title;
    uint public noOfContributors;
    uint public minimumContribution;
    uint public deadline;
    uint public goal;
    uint public raisedAmount;
    uint decimals = 10 ** 6;

    struct Request {
        string title;
        address recipient;
        uint amount;
        bool completed;
        uint noOfVoters;
        uint valueOfVotes;
        mapping(address => bool) voters;
    }

    event ContributeEvent(
        // address _projectAddress,
        address _contributor,
        address _tokenAddress,
        uint _amount
    );
    event GetRefundEvent(
        // address _projectAddress,
        address _contributor,
        address _RefundToken,
        uint _RefundAmount
    );
    event CreateRequestEvent(
        uint requestNum,
        // address _projectAddress,
        address _projectCreator,
        string _title,
        address _recipient,
        uint _amount
    );
    event ReceiveContributionEvent(
        // address _projectAddress,
        address _projectCreator,
        address _recipient,
        uint _amount,
        uint _requestNo
    );
    event voteRequestEvent(
        // address _projectAddress,
        address _voter,
        uint _requestNo,
        uint _valueOfVote
    );

    // cannot store Request in array as it contains mapping
    // therefore use mapping
    uint public numRequests;
    mapping(uint => Request) public requests;
    mapping(address => uint) public contributers;

    // allows factory to call the functions here
    modifier sharedOwner() {
        require(
            msg.sender == owner() || msg.sender == crowdfundingFactory,
            "You are neither the owner nor the factory"
        );
        _;
    }

    constructor(
        address _crowdfundingFactory,
        uint _goal,
        uint _deadline,
        address _acceptingThisToken,
        string memory _title
    ) {
        require(_deadline > block.timestamp);
        require(isToken(_acceptingThisToken), "Address provided is not token");
        require(
            isContract(_acceptingThisToken),
            "Address provided is not a contract"
        );
        title = _title;
        crowdfundingFactory = _crowdfundingFactory;
        goal = _goal;
        deadline = _deadline;
        minimumContribution = 100;
        admin = msg.sender;

        token = IERC20(_acceptingThisToken);
        tokenAddress = _acceptingThisToken;
    }

    function contribute(uint _amount) public {
        // require(block.timestamp < deadline, "Deadline has passed");
        require(_amount >= minimumContribution, "minimum contribution not met");

        require(
            token.allowance(msg.sender, address(this)) >= _amount,
            "token spend not approved"
        );

        if (contributers[msg.sender] == 0) {
            unchecked {
                noOfContributors++;
            }
        }
        token.transferFrom(msg.sender, address(this), _amount);
        unchecked {
            contributers[msg.sender] += _amount;
            raisedAmount += _amount;
        }

        emit ContributeEvent(msg.sender, tokenAddress, _amount);
    }

    function getBalance() public view returns (uint) {
        return token.balanceOf(address(this));
    }

    function getRefund() public returns (uint) {
        require(block.timestamp >= deadline, "Deadline has not passed");
        require(raisedAmount < goal, "Raised amount is more than goal");
        require(contributers[msg.sender] > 0, "You need to be a contributer");

        uint _amount = contributers[msg.sender];
        contributers[msg.sender] = 0;
        token.transfer(msg.sender, _amount);
        emit GetRefundEvent(msg.sender, address(token), _amount);
        return _amount;
    }

    function createRequest(
        string memory _title,
        address _recipient,
        uint _amount
    ) public onlyOwner {
        uint _requestNo = numRequests;
        Request storage newRequest = requests[numRequests];
        numRequests++;

        newRequest.title = _title;
        newRequest.recipient = _recipient;
        newRequest.amount = _amount;
        newRequest.completed = false;
        newRequest.noOfVoters = 0;

        emit CreateRequestEvent(
            _requestNo,
            msg.sender,
            _title,
            _recipient,
            _amount
        );
    }

    function voteRequest(uint _requestNo) public returns (uint) {
        require(numRequests >= _requestNo, "Request does not exist.");

        // owner of crowdfunding project cannot vote for their own requests
        require(
            msg.sender != owner(),
            "You're not allowed to vote for this request"
        );

        // must have contributed before voting
        require(
            contributers[msg.sender] > 0,
            "You must be a contributor to vote!"
        );

        Request storage thisRequest = requests[_requestNo];
        require(
            thisRequest.voters[msg.sender] == false,
            "You have already voted"
        );
        require(
            thisRequest.recipient != address(0),
            "Request has not been initiated"
        );

        // Ratio of the value of contribution by one user to the goal.
        // The higher the contribution the more the proportion in comparison to the goal.
        // Solidity has no float or double, therefore valueOfVotes is represents the ratio.
        uint _valueOfVote = (contributers[msg.sender] * decimals) / goal;
        thisRequest.valueOfVotes += _valueOfVote;
        thisRequest.noOfVoters++;
        thisRequest.voters[msg.sender] = true;
        emit voteRequestEvent(msg.sender, _requestNo, _valueOfVote);
        return _valueOfVote;
    }

    function receiveContribution(
        uint _requestNo
    ) public onlyOwner returns (address, uint) {
        require(
            raisedAmount >= goal,
            "raisedAmount must be more than or equal to goal"
        );

        Request storage thisRequest = requests[_requestNo];
        require(
            thisRequest.completed == false,
            "The request has been completed."
        );

        uint8 _numerator = 5;
        uint8 _denominator = 10;
        // value of votes more than 0.5 to pass
        require(
            thisRequest.valueOfVotes > (_numerator * decimals) / _denominator,
            "value of votes fail to meet requirement"
        );

        address _recipient = thisRequest.recipient;
        uint _amount = thisRequest.amount;
        token.transfer(_recipient, _amount);
        thisRequest.completed = true;
        emit ReceiveContributionEvent(
            msg.sender,
            _recipient,
            _amount,
            _requestNo
        );
        return (thisRequest.recipient, thisRequest.amount);
    }

    function getRequestTitle(
        uint _requestNo
    ) external view returns (string memory) {
        return requests[_requestNo].title;
    }

    function getRequestRecipient(
        uint _requestNo
    ) external view returns (address) {
        return requests[_requestNo].recipient;
    }

    function getRequestAmount(uint _requestNo) external view returns (uint) {
        return requests[_requestNo].amount;
    }

    function getRequestCompleted(uint _requestNo) external view returns (bool) {
        return requests[_requestNo].completed;
    }

    function getRequestNumOfVoters(
        uint _requestNo
    ) external view returns (uint) {
        return requests[_requestNo].noOfVoters;
    }

    function getRequestValueOfVotes(
        uint _requestNo
    ) external view returns (uint) {
        return requests[_requestNo].valueOfVotes;
    }

    function isContract(address _address) internal view returns (bool) {
        uint size;
        assembly {
            size := extcodesize(_address)
        }
        return size > 0;
    }

    // this function will run at constructor
    function isToken(address _address) internal view returns (bool) {
        ERC20 testAddress = ERC20(_address);
        require(testAddress.balanceOf(msg.sender) >= 0, "method doesn't exist");
        require(testAddress.totalSupply() > 0, "Supply is 0");
        bytes memory _testString = bytes(testAddress.name());

        if (_testString.length > 0) return true;
        return false;
    }
}
